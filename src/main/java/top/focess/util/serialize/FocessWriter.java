package top.focess.util.serialize;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * This class is used to serialize FocessSerializable-Object.
 */
public abstract class FocessWriter {

    /**
     * New a FocessWriter with given output stream
     * @param outputStream the given output stream
     * @return the FocessWriter with given output stream
     *
     * @throws IllegalStateException if the given output stream is not valid
     */
    @NotNull
    @Contract("_ -> new")
    public static FocessWriter newFocessWriter(final OutputStream outputStream) {
        return new SimpleFocessWriter() {

            @Override
            public void write(final Object o) {
                super.write(o);
                try {
                    outputStream.write(this.toByteArray());
                    outputStream.flush();
                    outputStream.close();
                } catch (final IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }
    /**
     * New a FocessWriter with given output stream and writer map
     * @param outputStream the given output stream
     * @param writerMap the given writer map
     * @return the FocessWriter with given output stream and writer map
     *
     * @throws IllegalStateException if the given output stream is not valid
     */
    @NotNull
    @Contract("_,_ -> new")
    public static FocessWriter newFocessWriter(final OutputStream outputStream, final Map<Class<?>, SimpleFocessWriter.Writer<?>> writerMap) {
        return new SimpleFocessWriter(writerMap) {
            @Override
            public void write(final Object o) {
                super.write(o);
                try {
                    outputStream.write(this.toByteArray());
                    outputStream.flush();
                    outputStream.close();
                } catch (final IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    /**
     * Write object by this writer
     * @param o the object need to be written
     *
     * @throws NotFocessSerializableException if the object is not FocessSerializable
     */
    public abstract void write(Object o);

    public abstract void writeString(String v);

    public abstract void writeInt(int v);

    public abstract void writeLong(long v);

    public abstract void writeFloat(float v);

    public abstract void writeDouble(double v);

    public abstract void writeBoolean(boolean v);

    public abstract void writeByte(byte v);

    public abstract void writeShort(short v);

    public abstract void writeChar(char v);

    public abstract <T> void writeObject(Object o);
}
