package io.storyclip.web.Restfull;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.storyclip.web.Common.JWTManager;
import io.storyclip.web.Common.Recaptcha;
import io.storyclip.web.Common.UserAgentParser;
import io.storyclip.web.Common.UserManager;
import io.storyclip.web.Entity.Result;
import io.storyclip.web.Entity.Token;
import io.storyclip.web.Entity.User;
import io.storyclip.web.Exception.ParamRequiredException;
import io.storyclip.web.Repository.TokenRepository;
import io.storyclip.web.Repository.UserRepository;
import io.storyclip.web.Type.Auth;
import io.storyclip.web.Type.Http;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.ConstructorProperties;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping(value="/auth")
public class AuthController {

    private static final String HEADER_TOKEN_KEY = "Bearer ";

    // Autowired 대신 추천되는 의존성 주입 방식
    private static TokenRepository TokenRepo;
    private static UserRepository UserRepo;

    @ConstructorProperties({"TokenRepository", "UserRepository"})
    public AuthController(TokenRepository TokenRepo, UserRepository UserRepo) {
        this.TokenRepo = TokenRepo;
        this.UserRepo = UserRepo;
    }

    @PutMapping("/refresh")
    public Result refreshToken(@RequestBody Token requestToken) throws ParamRequiredException {
        Result result = new Result();

        String refreshToken = requestToken.getRefreshToken();
        if(refreshToken == null) {
            throw new ParamRequiredException("Required params");
        }

        Token token = TokenRepo.findTokenByRefreshToken(refreshToken);

        if(token == null) {
            result.setSuccess(false);
            result.setMessage(Auth.JWT_EXPIRED_ERROR); // refresh_token 만료를 뜻함.
            return result;
        }

        User user = UserRepo.findUserByUserId(token.getUserId());

        Token newToken = JWTManager.create(user, token.getBrowser(), true);

        TokenRepo.delete(token);

        if(newToken == null) {
            // 토큰이 생성되지 못했음.
            result.setSuccess(false);
            result.setMessage(Auth.JWT_ERROR);
            result.setResult(null);
            return result;
        }

        result.setSuccess(true);
        result.setMessage(Auth.OK);
        result.setResult(newToken);
        return result;
    }

    @GetMapping(value="/verify")
    public Result verifyToken(@RequestHeader(required = false, value = "Authorization") String token) throws Exception {
        Result result = new Result();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("verify", false);

        try {
            HashMap<String, Object> info = JWTManager.read(token);
            hashMap.put("verify", true);
        } catch (Exception e) {
        }

        result.setSuccess(true);
        result.setMessage(Auth.OK);
        result.setResult(hashMap);

        return result;
    }

    @GetMapping(value="/key")
    public Result getPublicKey(@RequestHeader(value = "Authorization") String token) throws Exception {
        Result result = new Result();
        Token savedToken = TokenRepo.getTokenByToken(token.replace(HEADER_TOKEN_KEY, ""));
        if(savedToken == null) {
            throw new TokenExpiredException(null);
        }

        result.setSuccess(true);
        result.setMessage(Auth.OK);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("publicKey", savedToken.getPublicKey());

        result.setResult(hashMap);

        return result;
    }

    @PostMapping(value="/signin")
    public Result login(@RequestBody HashMap<String, Object> param, HttpServletRequest request) {
        Result result = new Result();

        String email = (String) param.get("email");
        String password = (String) param.get("password");
        String recaptchaToken = (String) param.get("recaptchaToken");
        Boolean autoLogin = param.containsKey("autoLogin") ? (Boolean) param.get("autoLogin") : false;

        if(email == null || password == null) {
            result.setSuccess(false);
            result.setMessage(Http.PARAM_REQUIRED);
            return result;
        }

        if(recaptchaToken == null) {
            result.setSuccess(false);
            result.setMessage(Auth.CAPTCHA_EMPTY);
            return result;
        }

        // 리캡챠 검증
        if(!Recaptcha.verify(recaptchaToken)){
            result.setSuccess(false);
            result.setMessage(Auth.CAPTCHA_FAIL);
            return result;
        }

        User user = UserManager.getUserbyEmailAndPassword(email, password);
        // 솔트 찾아서 해당 비밀번호로 조회

        if(user == null) {
            result.setSuccess(false);
            result.setMessage(Auth.AUTH_WRONG);
            return result;
        }

        user.setLastDate(new Date());
        user = UserRepo.save(user);

        result.setSuccess(true);
        result.setMessage(Auth.OK);

        Token token = JWTManager.create(user, UserAgentParser.getUserAgent(request), autoLogin);

        if(token == null) {
            // 토큰이 생성되지 못했음.
            result.setSuccess(false);
            result.setMessage(Auth.JWT_ERROR);
            result.setResult(null);
            return result;
        }

        result.setResult(token);
        return result;
    }

    @DeleteMapping("/signout")
    public Result disposalToken(@RequestHeader(value = "Authorization") String token) {
        Result result = new Result();

        TokenRepo.deleteByToken(token.replace(HEADER_TOKEN_KEY, ""));

        result.setSuccess(true);
        result.setMessage(Http.OK);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("signout", true);

        result.setResult(hashMap);
        return result;
    }
}
