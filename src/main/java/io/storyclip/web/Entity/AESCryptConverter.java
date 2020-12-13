package io.storyclip.web.Entity;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Convert
public class AESCryptConverter implements AttributeConverter<String, String> {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final byte[] KEY = "{ENCRYPT_KEY}".getBytes();
    private static final byte[] IV = "{ENCRYPT_IV}".getBytes();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        Key key = new SecretKeySpec(KEY, "AES");
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));
            return new String(Base64.getEncoder().encode(cipher.doFinal(attribute.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        Key key = new SecretKeySpec(KEY, "AES");
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

