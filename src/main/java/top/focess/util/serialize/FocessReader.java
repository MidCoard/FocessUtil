package top.focess.util.serialize;

import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * This class is used to deserialize FocessSerializable-Object.
 */
public abstract class FocessReader {

    /**
     * New a FocessReader with given input stream
     * @param inputStream the given input stream
     * @return the FocessReader with given input stream
     *
     * @throws IllegalStateException if the input stream is not valid
     */
    @NotNull
    @Contract("_ -> new")
    public static FocessReader newFocessReader(final InputStream inputStream) {
        final List<Byte> byteList = Lists.newArrayList();
        final byte[] bytes = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(bytes)) != -1)
                for (int i = 0; i < len; i++)
                    byteList.add(bytes[i]);
            inputStream.close();
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
        return new SimpleFocessReader(Bytes.toArray(byteList));
    }

    /**
     * New a FocessReader with given input stream and reader map
     * @param inputStream the given input stream
     * @param readerMap the given reader map
     * @return the FocessReader with given input stream and reader map
     *
     * @throws IllegalStateException if the input stream is not valid
     */
    @NotNull
    @Contract("_,_ -> new")
    public static FocessReader newFocessReader(final InputStream inputStream, final Map<Class<?>, SimpleFocessReader.Reader<?>> readerMap) {
        final List<Byte> byteList = Lists.newArrayList();
        final byte[] bytes = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(bytes)) != -1)
                for (int i = 0; i < len; i++)
                    byteList.add(bytes[i]);
            inputStream.close();
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
        return new SimpleFocessReader(Bytes.toArray(byteList), readerMap);
    }



    /**
     * Read object from the reader
     * @return the object read from the reader
     *
     * @throws SerializationParseException if the binary-data is not correct
     */
    public abstract Object read();

    public abstract String readString();

    public abstract int readInt();

    public abstract long readLong();

    public abstract float readFloat();

    public abstract double readDouble();

    public abstract boolean readBoolean();

    public abstract byte readByte();

    public abstract short readShort();

    public abstract char readChar();

    public abstract <T,V extends Enum<V>> Object readObject();


}
