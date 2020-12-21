package io.storyclip.web.Common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.storyclip.web.Encrypt.AES256Util;
import io.storyclip.web.Entity.Token;
import io.storyclip.web.Entity.User;
import io.storyclip.web.Encrypt.RSAUtils;
import io.storyclip.web.Repository.TokenRepository;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@Component
public class JWTManager {

    // Autowired 대신 추천되는 의존성 주입 방식
    private static TokenRepository TokenRepo;
    public JWTManager(TokenRepository TokenRepo) {
        this.TokenRepo = TokenRepo;
    }

    /**
     * 토큰을 생성하고 DB에 저장한다.
     * @param user 유저 정보
     * @param userAgent 토큰 생성을 요청한 유저 에이전트 정보
     * @return Token 객체
     */
    public static Token create(User user, String userAgent) {

        if(user.getUserId() <= 0) {
            // 사용자 고유값이 없으면 사용자 정보가 없다고 판정하고 토큰 생성하지 않음.
            return null;
        }

        try {
            RSAUtils rsa = new RSAUtils();
            Token token = new Token();

            // 현재 시간
            Date currentDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);

            // 토큰 생성
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) rsa.getPublicKey(), (RSAPrivateKey) rsa.getPrivateKey());
            JWTCreator.Builder tokenBuilder = JWT.create()
                    .withIssuer("api.storyclip.io") // 토큰 발급자
                    .withAudience("storyclip.io") // 토큰 수신자
//                    .withNotBefore(currentDate) // TODO: NOT_BEFORE 테스트 바람
                    .withIssuedAt(cal.getTime()); // 토큰 발급시간

            // access token 만료 시간은 24시간으로 설정
            cal.add(Calendar.DATE, 1);

            // 토큰 추가 데이터 입력
            tokenBuilder.withExpiresAt(cal.getTime()); // 토큰 만료시간

            // 토큰에 유저 정보 입력
            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", AES256Util.encrypt(Integer.toString(user.getUserId())));
            userInfo.put("email", user.getEmail());
            userInfo.put("penName", user.getPenName());
            userInfo.put("lastDate", user.getLastDate());
            userInfo.put("profile", user.getProfile());

            tokenBuilder.withClaim("userInfo", userInfo);
            String tokenStr = tokenBuilder.sign(algorithm); // 토큰에 사이닝

            // 토큰 정보 디비에 저장 준비
            token.setUserId(user.getUserId());
            token.setToken(tokenStr);
            token.setExpireDate(cal.getTime());
            token.setPublicKey(rsa.getPublic());
            token.setPrivateKey(rsa.getPrivate());
            token.setBrowser(userAgent);

            // refresh token 만료시간은 2주로 설정
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, 14);

            // 토큰 정보 디비에 저장 준비 2
            token.setRefreshExpireDate(cal.getTime());

            System.out.println("##### public key\n"+rsa.getPublic());
            System.out.println("##### private key\n"+rsa.getPrivate());

//            return TokenRepo.save(token); // TODO: 디비에 저장해봐야됨
            return token;

        } catch (JWTCreationException e){
            //Invalid Signing configuration / Couldn't convert Claims.
            // throw new RuntimeException(e);
            return null;
        }
    }
    
    // TODO: 토큰 검증 메소드 추가 및 토큰 검증 실패 유형에 따른 다양한 리턴
//    public static String verify(String token) {
//        try {
//            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer("auth0")
//                    .build(); //Reusable verifier instance
//            DecodedJWT jwt = verifier.verify(token);
//        } catch (JWTVerificationException exception){
//            //Invalid signature/claims
//        }
//    }
}
