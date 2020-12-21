package io.storyclip.web.Type;

public enum Auth {
    /**
     * JWT 암호화 비밀키가 없음 : 서버 에러
     */
    JWT_KEY_EMPTY,

    /**
     * JWT 암호화 공용키가 없음 : 서버 에러
     */
    JWT_PUB_KEY_EMPTY,

    /**
     * JWT 생성 실패 : 서버 에러
     */
    JWT_ERROR,

    /**
     * JWT 만료됨
      */
    JWT_EXPIRED_ERROR,

    /**
     * JWT 검증 실패
     */
    JWT_VERIFY_ERROR,

    /**
     * JWT 아직 활성화되지 않음
     */
    JWT_BEFORE_ERROR,

    /**
     * 잘못된 계정 정보 (아이디 혹은 비밀번호)
     */
    AUTH_WRONG,

    /**
     * 로그인 필요
     */
    AUTH_REQURED,

    /**
     * 계정정보 중복됨
     */
    JOIN_DUPLICATE,

    /**
     * 캡챠 토큰 없음
     */
    CAPTCHA_EMPTY,

    /**
     * 캡챠 실패
     */
    CAPTCHA_FAIL,

    /**
     * 성공
     */
    OK
}