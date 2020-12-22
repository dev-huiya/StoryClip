package io.storyclip.web.Encrypt;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class SHA256Util {
    public static String encrypt(String planText) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<byteData.length;i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 솔트를 생성하는 메소드.
     *
     * @param length 솔트 길이
     * @return
     */
    public static String createSalt(Integer length) {
        final Random r = new SecureRandom();
        byte[] salt = new byte[length];
        r.nextBytes(salt);
        Base64.Encoder encoder = Base64.getEncoder();
        return new String(encoder.encode(salt));
    }

    /**
     * Byte array to HEX
     *
     * @param str byte array
     * @return
     */
    public static String byteArrayToHex(byte[] str) {
        StringBuilder sb = new StringBuilder();
        for(final byte b: str)
            sb.append(String.format("%02x", b&0xff));
        return sb.toString();
    }
}