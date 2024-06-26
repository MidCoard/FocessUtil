package top.focess.util.serialize;

import com.google.common.collect.Maps;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static top.focess.util.serialize.Opcodes.*;

public class SimpleFocessReader extends FocessReader {

    private static ClassFinder DEFAULT_CLASS_FINDER = Class::forName;

    private static final PureJavaReflectionProvider PROVIDER = new PureJavaReflectionProvider();

    private static final Map<Class<?>, Reader<?>> CLASS_READER_MAP = Maps.newHashMap();

    static {

        CLASS_READER_MAP.put(Class.class, (Reader<Class>) (t, reader) -> {
            try {
                final String cls = reader.readString();
                switch (cls) {
                    case "byte":
                        return byte.class;
                    case "short":
                        return short.class;
                    case "int":
                        return int.class;
                    case "long":
                        return long.class;
                    case "float":
                        return float.class;
                    case "double":
                        return double.class;
                    case "boolean":
                        return boolean.class;
                    case "char":
                        return char.class;
                    case "void":
                        return void.class;
                    default:
                        return DEFAULT_CLASS_FINDER.forName(cls);
                }
            } catch (final ClassNotFoundException e) {
                throw new SerializationParseException(e);
            }
        });
    }

    private final byte[] bytes;

    private final Map<Class<?>, Reader<?>> readerMap = Maps.newHashMap();

    private int pointer;

    public SimpleFocessReader(final byte[] bytes, final Map<Class<?>, Reader<?>> readerMap) {
        this.bytes = bytes;
        this.pointer = 0;
        this.readerMap.putAll(readerMap);
    }

    protected SimpleFocessReader(final byte[] bytes) {
        this.bytes = bytes;
        this.pointer = 0;
    }

    public static ClassFinder getDefaultClassFinder() {
        return DEFAULT_CLASS_FINDER;
    }

    public static void setDefaultClassFinder(ClassFinder defaultClassFinder) {
        DEFAULT_CLASS_FINDER = defaultClassFinder;
    }

    public int readInt() {
        int r = 0;
        for (int i = 0; i < 4; i++)
            r += (Byte.toUnsignedInt(this.bytes[this.pointer++]) << (i * 8));
        return r;
    }

    public long readLong() {
        long r = 0L;
        for (int i = 0; i < 8; i++)
            r += (Byte.toUnsignedLong(this.bytes[this.pointer++]) << (i * 8L));
        return r;
    }

    public String readString() {
        final int length = this.readInt();
        final byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++)
            bytes[i] = this.bytes[this.pointer++];
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(this.readLong());
    }

    public byte readByte() {
        return this.bytes[this.pointer++];
    }

    public short readShort() {
        short r = 0;
        for (int i = 0; i < 2; i++)
            // still the right side is short even if not cast to short
            // because two bytes are used to represent a short
            r += (short) ((short) Byte.toUnsignedInt(this.bytes[this.pointer++]) << (i * 8));
        return r;
    }

    public char readChar() {
        return (char) this.readShort();
    }

    public boolean readBoolean() {
        return this.readByte() == 1;
    }

    @Nullable
    public Object read() {
        if (this.pointer >= this.bytes.length)
            throw new SerializationParseException("Read over");
        final byte start = this.readByte();
        if (start != C_START)
            throw new SerializationParseException("Start code is not correct");
        final Object o = this.readObject();
        final byte end = this.readByte();
        if (end != C_END)
            throw new SerializationParseException("End code is not correct");
        return o;
    }

    private Class<?> readClass() {
        final String cls = this.readString();
        switch (cls) {
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "boolean":
                return boolean.class;
            case "char":
                return char.class;
            case "void":
                return void.class;
            default:
                try {
                    return DEFAULT_CLASS_FINDER.forName(cls);
                } catch (final ClassNotFoundException e) {
                    throw new SerializationParseException(e);
                }
        }
    }

    @Nullable
    public <T,V extends Enum<V>> Object readObject() {
        final byte type = this.readByte();
        switch (type) {
            case C_NULL:
                return null;
            case C_BYTE:
                return this.readByte();
            case C_SHORT:
                return this.readShort();
            case C_INT:
                return this.readInt();
            case C_LONG:
                return this.readLong();
            case C_FLOAT:
                return this.readFloat();
            case C_DOUBLE:
                return this.readDouble();
            case C_BOOLEAN:
                return this.readBoolean();
            case C_CHAR:
                return this.readChar();
            case C_STRING:
                return this.readString();
            case C_ENUM: {
                try {
                    final Class<V> cls = (Class<V>) this.readClass();
                    return Enum.valueOf(cls, this.readString());
                } catch (final Exception e) {
                    throw new SerializationParseException(e);
                }
            }
            case C_ARRAY: {
                final Class<?> cls = this.readClass();
                final int length = this.readInt();
                final Object array = Array.newInstance(cls, length);
                for (int i = 0; i < length; i++)
                    Array.set(array, i, this.readObject());
                return array;
            }
            case C_FSERIALIZABLE: {
                final String className = this.readString();
                final Object o = this.readObject();
                if (o instanceof Map)
                    try {
                        final Class<?> cls = DEFAULT_CLASS_FINDER.forName(className);
                        final Method method = cls.getMethod("deserialize", Map.class);
                        return method.invoke(null, o);
                    } catch (final Exception e) {
                        throw new SerializationParseException(e);
                    }
                else throw new SerializationParseException("Deserialize argument is not a map");
            }
            case C_OBJECT: {
                final String className = this.readString();
                final int length = this.readInt();
                try {
                    final Class<?> cls = DEFAULT_CLASS_FINDER.forName(className);
                    final Object o = PROVIDER.newInstance(cls);
                    for (int i = 0; i < length; i++) {
                        final byte field = this.readByte();
                        if (field != C_FIELD)
                            throw new SerializationParseException("Field code is not correct");
                        final String fieldName = this.readString();
                        final Field f = cls.getDeclaredField(fieldName);
                        f.setAccessible(true);
                        f.set(o, this.readObject());
                    }
                    return o;
                } catch (final Exception e) {
                    throw new SerializationParseException(e);
                }
            }
            case C_RESERVED: {
                final String className = this.readString();
                try {
                    final Class<T> cls = (Class<T>) DEFAULT_CLASS_FINDER.forName(className);
                    Reader<T> reader;
                    if ((reader = (Reader<T>) CLASS_READER_MAP.get(cls)) != null)
                        return reader.read(cls, this);
                    else if ((reader = (Reader<T>) this.readerMap.get(cls)) != null)
                        return reader.read(cls, this);
                    else throw new SerializationParseException("No reader for class: " + className);
                } catch (final ClassNotFoundException e) {
                    throw new SerializationParseException(e);
                }
            }
            case C_SERIALIZABLE: {
                try {
                    final byte[] bytes = (byte[]) this.readObject();
                    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    return objectInputStream.readObject();
                } catch (final Exception e) {
                    throw new SerializationParseException(e);
                }
            }
            default:
                throw new SerializationParseException("Unknown type code");
        }
    }

    public interface Reader<T> {
        T read(Class<T> cls, FocessReader reader) throws SerializationParseException;
    }

}
