package top.focess.util.serialize;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Bytes;
import top.focess.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static top.focess.util.serialize.Opcodes.*;

public class SimpleFocessWriter extends FocessWriter {

    private static final Map<Class<?>, Writer<?>> CLASS_WRITER_MAP = Maps.newHashMap();

    static {
        SimpleFocessWriter.CLASS_WRITER_MAP.put(Class.class, (Writer<Class>) (clazz, writer) -> writer.writeString(clazz.getName()));
    }

    private final Map<Class<?>, Writer<?>> writerMap = Maps.newHashMap();

    private final List<Byte> data = Lists.newArrayList();

    protected SimpleFocessWriter() {}

    public SimpleFocessWriter(final Map<Class<?>, Writer<?>> writerMap) {
        this.writerMap.putAll(writerMap);
    }

    public void writeInt(int v) {
        for (int i = 0; i < 4; i++) {
            this.data.add((byte) (v & 0xFF));
            v >>>= 8;
        }
    }

    public void writeLong(long v) {
        for (int i = 0; i < 8; i++) {
            this.data.add((byte) (v & 0xFFL));
            v >>>= 8;
        }
    }

    public void writeString(final String v) {
        final byte[] bytes = v.getBytes(StandardCharsets.UTF_8);
        this.writeInt(bytes.length);
        this.data.addAll(Bytes.asList(bytes));
    }

    public void writeFloat(final float v) {
        this.writeInt(Float.floatToIntBits(v));
    }

    public void writeDouble(final double v) {
        this.writeLong(Double.doubleToLongBits(v));
    }

    public void writeShort(short v) {
        for (int i = 0; i < 2; i++) {
            this.data.add((byte) (v & 0xFF));
            v >>>= 8;
        }
    }

    public void writeBoolean(final boolean v) {
        this.data.add((byte) (v ? 1 : 0));
    }

    public void writeChar(final char v) {
        this.writeShort((short) v);
    }

    public void writeByte(final byte v) {
        this.data.add(v);
    }

    private void writeClass(final Class<?> cls, final boolean isSerializable) {
        if (cls.equals(Byte.class))
            this.writeByte(C_BYTE);
        else if (cls.equals(Short.class))
            this.writeByte(C_SHORT);
        else if (cls.equals(Integer.class))
            this.writeByte(C_INT);
        else if (cls.equals(Long.class))
            this.writeByte(C_LONG);
        else if (cls.equals(Float.class))
            this.writeByte(C_FLOAT);
        else if (cls.equals(Double.class))
            this.writeByte(C_DOUBLE);
        else if (cls.equals(Boolean.class))
            this.writeByte(C_BOOLEAN);
        else if (cls.equals(Character.class))
            this.writeByte(C_CHAR);
        else if (cls.equals(String.class))
            this.writeByte(C_STRING);
        else if (cls.isArray())
            this.writeByte(C_ARRAY);
        else if (cls.isEnum()) {
            this.writeByte(C_ENUM);
            this.writeString(cls.getName());
        } else if (cls.getSuperclass().isEnum()) {
            this.writeByte(C_ENUM);
            this.writeString(cls.getSuperclass().getName());
        } else if (FocessSerializable.class.isAssignableFrom(cls)) {
            if (isSerializable)
                this.writeByte(C_FSERIALIZABLE);
            else this.writeByte(C_OBJECT);
            this.writeString(cls.getName());
        } else if (SimpleFocessWriter.CLASS_WRITER_MAP.containsKey(cls) || this.writerMap.containsKey(cls)) {
            this.writeByte(C_RESERVED);
            this.writeString(cls.getName());
        } else if (Serializable.class.isAssignableFrom(cls))
            this.writeByte(C_SERIALIZABLE);
        else throw new NotFocessSerializableException(cls.getName());
    }

    public <T> void writeObject(final Object o) {
        if (o == null) {
            this.writeByte(C_NULL);
            return;
        }
        final boolean isSerializable = o instanceof FocessSerializable;
        final Map<String, Object> data = isSerializable ? ((FocessSerializable) o).serialize() : null;
        this.writeClass(o.getClass(), data != null);
        if (o instanceof Byte)
            this.writeByte((Byte) o);
        else if (o instanceof Short)
            this.writeShort((Short) o);
        else if (o instanceof Integer)
            this.writeInt((Integer) o);
        else if (o instanceof Long)
            this.writeLong((Long) o);
        else if (o instanceof Float)
            this.writeFloat((Float) o);
        else if (o instanceof Double)
            this.writeDouble((Double) o);
        else if (o instanceof Boolean)
            this.writeBoolean((Boolean) o);
        else if (o instanceof String)
            this.writeString((String) o);
        else if (o instanceof Character)
            this.writeChar((Character) o);
        else if (o.getClass().isEnum() || o.getClass().getSuperclass().isEnum())
            this.writeString(((Enum<?>) o).name());
        else if (o instanceof FocessSerializable) {
            if (data != null)
                this.writeObject(data);
            else {
                final List<Field> fields = Stream.of(o.getClass().getDeclaredFields()).filter(f -> (f.getModifiers() & (Modifier.TRANSIENT | Modifier.STATIC)) == 0).collect(Collectors.toList());
                this.writeInt(fields.size());
                fields.forEach(f -> {
                    f.setAccessible(true);
                    try {
                        this.writeField(f.getName(), f.get(o));
                    } catch (final IllegalAccessException e) {
                        throw new SerializationException(e);
                    }
                });
            }
        } else if (o.getClass().isArray()) {
            this.writeString(o.getClass().getComponentType().getName());
            final int length;
            this.writeInt(length = Array.getLength(o));
            for (int i = 0; i < length; i++)
                this.writeObject(Array.get(o, i));
        } else if (SimpleFocessWriter.CLASS_WRITER_MAP.containsKey(o.getClass())) {
            final Writer<T> writer = (Writer<T>) CLASS_WRITER_MAP.get(o.getClass());
            writer.write((T) o, this);
        } else if (this.writerMap.containsKey(o.getClass())) {
            final Writer<T> writer = (Writer<T>) this.writerMap.get(o.getClass());
            writer.write((T) o, this);
        } else if (o instanceof Serializable) {
            try {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(o);
                objectOutputStream.close();
                this.writeObject(byteArrayOutputStream.toByteArray());
            } catch (final Exception e) {
                throw new SerializationException(e);
            }
        } else throw new NotFocessSerializableException(o.getClass().getName());
    }

    private void writeField(final String name, final Object o) {
        this.writeByte(C_FIELD);
        this.writeString(name);
        this.writeObject(o);
    }

    public void write(final Object o) {
        this.writeByte(C_START);
        this.writeObject(o);
        this.writeByte(C_END);
    }

    private void writeByte(final Byte o) {
        this.data.add(o);
    }

    public byte[] toByteArray() {
        return Bytes.toArray(this.data);
    }

    public interface Writer<T> {
        void write(T t, FocessWriter writer) throws NotFocessSerializableException;
    }
}
