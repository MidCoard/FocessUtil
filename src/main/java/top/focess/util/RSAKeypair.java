package top.focess.util;

/**
 * This class is used to store a RSA Keypair.
 */
public class RSAKeypair extends Pair<String,String>{

    /**
     * Constructs a new RSAKeypair with the given public key and private key.
     * @param publicKey the RSA public key
     * @param privateKey the RSA private key
     */
    public RSAKeypair(String publicKey, String privateKey) {
        super(publicKey, privateKey);
    }

    /**
     * Get the public key
     * @return the public key
     */
    public String getPublicKey() {
        return this.getFirst();
    }

    /**
     * Get the private key
     * @return the private key
     */
    public String getPrivateKey() {
        return this.getSecond();
    }
}
