package top.focess.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestRSA {

    @Test
    public void testRSA() {
        RSAKeypair keypair = RSA.genRSAKeypair();
        String publicKey = keypair.getPublicKey();
        String privateKey = keypair.getPrivateKey();
        String message = "Hello World!";
        String encrypted = RSA.encryptRSA(message, publicKey);
        String decrypted = RSA.decryptRSA(encrypted, privateKey);
        Assertions.assertEquals(message, decrypted);
    }

    @Test
    public void testRSASign() {
        RSAKeypair keypair = RSA.genRSAKeypair();
        String publicKey = keypair.getPublicKey();
        String privateKey = keypair.getPrivateKey();
        String message = "Hello World!";
        String sign = RSA.sign(message, privateKey);
        Assertions.assertTrue(RSA.verify(message, publicKey, sign));
    }
}
