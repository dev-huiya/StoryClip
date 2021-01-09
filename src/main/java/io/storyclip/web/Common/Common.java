package io.storyclip.web.Common;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class Common {


    /**
     * 안전한 랜덤 문자열을 생성하는 메소드.
     *
     * @param length 길이
     * @return base64 인코딩된 SecureRandom byte
     */
    public static String createSecureRandom(Integer length) {
        final Random r = new SecureRandom();
        byte[] salt = new byte[length];
        r.nextBytes(salt);
        Base64.Encoder encoder = Base64.getEncoder();
        return new String(encoder.encode(salt));
    }

    /**
     * 무작위 대소문자를 생성하는 메소드
     *
     * @param length 길이
     * @return 랜덤한 대소문자
     */
    public static String createRandom(Integer length) {
        Random rnd =new Random();
        String random = "";

        for(int i=0;i<length;i++) {
            if(rnd.nextBoolean()) {
                random += (char)((int)(rnd.nextInt(26))+97); // 소문자
            } else {
                random += (char)((int)(rnd.nextInt(26))+65); // 대문자
            }
        }

        return random;
    }

    /**
     * 숫자를 포함한 무작위 대소문자를 생성하는 메소드
     *
     * @param length 길이
     * @return 숫자를 포함한 랜덤한 대소문자
     */
    public static String createRandomWithInt(Integer length) {
        Random rnd = new Random();
        StringBuffer buf = new StringBuffer();
        for(int i=0;i<length;i++){
            // rnd.nextBoolean() 는 랜덤으로 true, false 를 리턴. true일 시 랜덤 한 소문자를, false 일 시 랜덤 한 숫자를 StringBuffer 에 append 한다.
            if(rnd.nextBoolean()){
                if(rnd.nextBoolean()) {
                    buf.append((char)((int)(rnd.nextInt(26))+97)); // 소문자
                } else {
                    buf.append((char)((int)(rnd.nextInt(26))+65)); // 대문자
                }
            }else{
                buf.append((rnd.nextInt(10)));
            }
        }

        return new String(buf);
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
