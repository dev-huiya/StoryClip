package io.storyclip.web.Restfull;

import io.storyclip.web.Common.JWTManager;
import io.storyclip.web.Common.UserAgentParser;
import io.storyclip.web.Entity.Result;
import io.storyclip.web.Entity.Token;
import io.storyclip.web.Entity.User;
import io.storyclip.web.Exception.ParamRequiredException;
import io.storyclip.web.Repository.TokenRepository;
import io.storyclip.web.Repository.UserRepository;
import io.storyclip.web.Type.Auth;
import io.storyclip.web.Type.Http;
import org.springframework.web.bind.annotation.*;

import java.beans.ConstructorProperties;
import java.util.HashMap;

@RestController
@RequestMapping(value="/auth")
public class AuthController {

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

    // TODO: logout으로 토큰 폐기하는 API 필요함.

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
}
