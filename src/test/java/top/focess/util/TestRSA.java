package top.focess.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import top.focess.util.network.HttpResponse;
import top.focess.util.network.HttpResponseException;
import top.focess.util.network.NetworkHandler;

import java.nio.charset.StandardCharsets;

public class TestRSA {

    @Test
    public void testRSA() {
        RSAKeypair keypair = RSA.genRSAKeypair();
        String publicKey = keypair.getPublicKey();
        String privateKey = keypair.getPrivateKey();
        String message = "中文!";
        byte[] encrypted = RSA.encryptRSA(message.getBytes(), publicKey);
        String decrypted = new String(RSA.decryptRSA(encrypted, privateKey));
        Assertions.assertEquals(message, decrypted);
    }

    @Test
    public void testRSASign() {
        RSAKeypair keypair = RSA.genRSAKeypair();
        String publicKey = keypair.getPublicKey();
        String privateKey = keypair.getPrivateKey();
        String message = "Hello World!";
        byte[] sign = RSA.sign(message.getBytes(), privateKey);
        Assertions.assertTrue(RSA.verify(message.getBytes(), publicKey, sign));
    }

    @Test
    public void testBase64() {
        String message = "Hello World!";
        byte[] encoded = Base64.encodeBase64(message.getBytes(StandardCharsets.ISO_8859_1));
        String decoded = new String(Base64.decodeBase64(encoded), StandardCharsets.ISO_8859_1);
        Assertions.assertEquals(message, decoded);
    }

    @Test
    public void testNetworkHandler() throws HttpResponseException {
        NetworkHandler networkHandler = new NetworkHandler();
        HttpResponse response = networkHandler.request("https://www.baidu.com", NetworkHandler.RequestType.GET);
        System.out.println(response.getAsString());
    }

}
