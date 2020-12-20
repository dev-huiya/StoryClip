package io.storyclip.web.Common;

import io.storyclip.web.Entity.Token;
import io.storyclip.web.Entity.User;
import io.storyclip.web.Encrypt.RSAUtils;


public class JWTManager {

    /**
     * 토큰을 생성하고 DB에 저장한다.
     * @param user 유저 정보
     * @param userAgent 토큰 생성을 요청한 유저 에이전트 정보
     * @return Token 객체
     */
    public static Token create(User user, String userAgent) {

        RSAUtils rsa = new RSAUtils();
        RSAUtils rsa2 = new RSAUtils();

        System.out.println("#### RSA 1 PUBLIC");
        System.out.println(rsa.getPublic());

//        System.out.println("\n");

        System.out.println("#### RSA 1 PRIVATE");
        System.out.println(rsa.getPrivate());

        System.out.println("\n\n\n\n");

        System.out.println("#### RSA 2 PUBLIC");
        System.out.println(rsa2.getPublic());

//        System.out.println("\n");

        System.out.println("#### RSA 2 PRIVATE");
        System.out.println(rsa2.getPrivate());

        // TODO: 함수 완성 바람.

        return null;
    }
}
