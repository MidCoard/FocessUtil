package top.focess.util;

import org.apache.commons.io.output.ByteArrayOutputStream;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * This is an RSA util class.
 */
public class RSA {

    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final KeyPairGenerator RSA_KEY_PAIR_GENERATOR;
    private static final KeyFactory RSA_KEY_FACTORY;

    static {
        try {
            RSA_KEY_PAIR_GENERATOR = KeyPairGenerator.getInstance("RSA");
            RSA_KEY_FACTORY = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        RSA_KEY_PAIR_GENERATOR.initialize(1024);
    }


    /**
     * Generate RSA key pair
     *
     * @return the RSA key pair
     */
    public static RSAKeypair genRSAKeypair() {
        KeyPair keyPair = RSA_KEY_PAIR_GENERATOR.generateKeyPair();
        String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()),StandardCharsets.ISO_8859_1);
        String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()), StandardCharsets.ISO_8859_1);
        return new RSAKeypair(publicKey, privateKey);
    }

    private static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes(StandardCharsets.ISO_8859_1));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return RSA_KEY_FACTORY.generatePublic(keySpec);
    }

    /**
     * Encrypt data with RSA public key
     *
     * @param data the data to encrypt
     * @param key the RSA public key
     * @return the encrypted data
     */
    public static byte[] encryptRSA(byte[] data, String key) {
        try {
            PublicKey publicKey = getPublicKey(key);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return Base64.encodeBase64(encryptedData);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    private static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes(StandardCharsets.ISO_8859_1));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return RSA_KEY_FACTORY.generatePrivate(keySpec);
    }

    /**
     * Decrypt data with RSA private key
     *
     * @param data the data to decrypt
     * @param key the RSA private key
     * @return the decrypted data
     */
    public static byte[] decryptRSA(byte[] data,String key) {
        try {
            PrivateKey privateKey = getPrivateKey(key);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] dataBytes = Base64.decodeBase64(data);
            int inputLen = dataBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * Sign data with RSA private key
     *
     * @param data the data to sign
     * @param k the RSA private key
     * @return the signed data
     */
    public static byte[] sign(byte[] data, String k) {
        try {
            byte[] keyBytes = getPrivateKey(k).getEncoded();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey key = RSA_KEY_FACTORY.generatePrivate(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(key);
            signature.update(data);
            return Base64.encodeBase64(signature.sign());
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * Verify sign with RSA public key
     *
     * @param data the data to verify
     * @param k the RSA public key
     * @param sign the sign to verify
     * @return true if the sign is valid, false otherwise
     */
    public static boolean verify(byte[] data, String k, byte[] sign) {
        try {
            byte[] keyBytes = getPublicKey(k).getEncoded();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey key = RSA_KEY_FACTORY.generatePublic(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(key);
            signature.update(data);
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            return false;
        }
    }
}
