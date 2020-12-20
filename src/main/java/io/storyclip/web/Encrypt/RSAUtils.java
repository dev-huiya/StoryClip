package io.storyclip.web.Encrypt;

import java.security.*;
import java.util.Base64;

public class RSAUtils {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public RSAUtils() {
            try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();

            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPublic() {
        return "-----BEGIN RSA PUBLIC KEY-----\n" + new String(Base64.getEncoder().encode(publicKey.getEncoded())) + "\n-----END RSA PUBLIC KEY-----\n";
    }

    public String getPrivate() {
        return "-----BEGIN RSA PRIVATE KEY-----\n" + new String(Base64.getEncoder().encode(privateKey.getEncoded())) + "\n-----END RSA PRIVATE KEY-----\n";
    }

    // TODO: 암복호화 수행 메소드 필요함.
}
