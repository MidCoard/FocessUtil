package top.focess.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * This is a Base64 util class.
 */
public class Base64 {

    /**
     * Used to encode the data with base64
     *
     * @param bytes the data need to be encoded
     * @return the encoded data with base64
     */
    @NotNull
    @Contract("_ -> new")
    public static byte[] encodeBase64(final byte[] bytes) {
        return java.util.Base64.getEncoder().encode(bytes);
    }

    /**
     * Used to decode the data with base64
     *
     * @param value the data need to be decoded
     * @return the decoded data with base64
     */
    public static byte[] decodeBase64(final byte[] value) {
        return java.util.Base64.getDecoder().decode(value);
    }
}
