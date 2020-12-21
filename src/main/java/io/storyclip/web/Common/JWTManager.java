package io.storyclip.web.Common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.storyclip.web.Encrypt.AES256Util;
import io.storyclip.web.Encrypt.RSAUtils;
import io.storyclip.web.Encrypt.SHA256Util;
import io.storyclip.web.Entity.Result;
import io.storyclip.web.Entity.Token;
import io.storyclip.web.Entity.User;
import io.storyclip.web.Repository.TokenRepository;
import io.storyclip.web.Type.Auth;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
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
    public static Token create(User user, String userAgent, Boolean isAutoLogin) {

        if(user.getUserId() <= 0) {
            // 사용자 고유값이 없으면 사용자 정보가 없다고 판정하고 토큰 생성하지 않음.
            return null;
        }

        try {
            RSAUtils rsa = new RSAUtils();
            Token token = new Token();

            Date refreshExpireDate = null;
            String refreshToken = null;

            // 현재 시간
            Date currentDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);

            // access token 만료 시간은 24시간으로 설정
            cal.add(Calendar.DATE, 1);
            Date accessExpireDate = cal.getTime();

            if(isAutoLogin) {
                // 자동 로그인이 켜져있으면 refresh_token 발급

                // refresh token 만료시간은 2주로 설정
                cal.setTime(currentDate);
                cal.add(Calendar.DATE, 14);
                refreshExpireDate = cal.getTime();
                refreshToken = createRefreshToken();
            }

            // 토큰에 넣을 유저 정보 준비
            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", AES256Util.encrypt(Integer.toString(user.getUserId())));
            userInfo.put("email", user.getEmail());
            userInfo.put("penName", user.getPenName());
            userInfo.put("lastDate", user.getLastDate());
            userInfo.put("profile", user.getProfile());
            userInfo.put("refreshToken", refreshToken);
            userInfo.put("refreshExpireDate", refreshExpireDate);

            // 토큰 생성
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) rsa.getPublicKey(), (RSAPrivateKey) rsa.getPrivateKey());
            String tokenStr = JWT.create()
                    .withIssuer("api.storyclip.io") // 토큰 발급자
                    .withAudience("storyclip.io") // 토큰 수신자
                    //.withNotBefore(refreshExpireDate) // 토큰 활성화 되는 시간 (미사용 예정)
                    .withIssuedAt(currentDate) // 토큰 발급시간
                    .withExpiresAt(accessExpireDate) // 토큰 만료시간
                    .withClaim("userInfo", userInfo) // 유저 정보 토큰에 넣기
                    .sign(algorithm); // 토큰에 사이닝

            // 토큰 정보 디비에 저장 준비
            token.setUserId(user.getUserId());
            token.setToken(tokenStr);
            token.setExpireDate(cal.getTime());
            token.setPublicKey(rsa.getPublic());
            token.setPrivateKey(rsa.getPrivate());
            token.setBrowser(userAgent);
            token.setRefreshExpireDate(refreshExpireDate);
            token.setRefreshToken(refreshToken);

            return TokenRepo.save(token);
        } catch (JWTCreationException e){
            //Invalid Signing configuration / Couldn't convert Claims.
            // throw new RuntimeException(e);
            return null;
        }
    }

    /**
     * 키를 DB에서 불러와 토큰을 검증한다.
     * @param token 토큰
     * @return
     */
    public static Result verify(String token) {
        Result result = new Result();

        Token savedToken = TokenRepo.getTokenByToken(token);
        if(savedToken == null) {
            // 디비에 저장된 키가 없으면 false 리턴
            result.setSuccess(false);
            result.setMessage(Auth.JWT_KEY_EMPTY);
            result.setResult(null);
            return result;
        }

        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");

            String publicKeyStr = savedToken.getPublicKey().replace("-----BEGIN RSA PUBLIC KEY-----\n", "").replace("\n-----END RSA PUBLIC KEY-----\n", "");
            String privateKeyStr = savedToken.getPrivateKey().replace("-----BEGIN RSA PRIVATE KEY-----\n", "").replace("\n-----END RSA PRIVATE KEY-----\n", "");

            // public key 불러오기
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
            PublicKey publicKey = kf.generatePublic(publicSpec);

            // private key 불러오기
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
            PrivateKey privateKey = kf.generatePrivate(spec);

            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("api.storyclip.io")
                    .build(); //Reusable verifier instance

            DecodedJWT jwt = verifier.verify(token);

            result.setSuccess(true);
            result.setMessage(Auth.OK);
            result.setResult(jwt);
            return result;
        } catch (NoSuchAlgorithmException exception){
            // 알고리즘을 찾을 수 없음. 사실상 발생하지 않을 예정인 오류
            result.setSuccess(false);
            result.setMessage(Auth.JWT_ALGORITHM_ERROR);
            result.setResult(null);
            return result;
        } catch (InvalidKeySpecException | SignatureVerificationException e) {
            // 키 불일치 오류
            result.setSuccess(false);
            result.setMessage(Auth.JWT_KEY_EMPTY);
            result.setResult(null);
            return result;
        } catch (TokenExpiredException e) {
            // 토큰 만료 오류
            result.setSuccess(false);
            result.setMessage(Auth.JWT_EXPIRED_ERROR);
            result.setResult(null);
            return result;
        } catch (InvalidClaimException e) {
            // Claim 오류. (ex. 발급자가 일치하지 않음)
            result.setSuccess(false);
            result.setMessage(Auth.JWT_INVALID_CLAIM);
            result.setResult(null);
            return result;
        } catch (JWTVerificationException e) {
            // 그외 다양한 검증 오류
            result.setSuccess(false);
            result.setMessage(Auth.JWT_VERIFY_ERROR);
            result.setResult(null);
            return result;
        }
    }

    public static String createRefreshToken() {
        String random = SHA256Util.getSalt(20);
        Token result = TokenRepo.findTokenByRefreshToken(random);

        if(result != null) {
            return createRefreshToken();
        } else {
            return random;
        }
    }
}
