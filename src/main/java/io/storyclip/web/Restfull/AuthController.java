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

    // ############################# 이 밑은 테스트 코드

    @GetMapping(value="/verify")
    public Result verifyToken(@RequestHeader("Authorization") String token) throws Exception {
        Result result = new Result();

//        System.out.println(token);

        HashMap<String, Object> info = JWTManager.read(token);
        // 읽었을때 만료나 일치하지 않는 토큰이 들어올 수가 없음.
        // 인터셉터에서 이미 한번 검증을 거쳐서 올라오는거라 만료된 토큰을 인터셉터 단에서 걸러버림
        // 2020-12-22 16:38 hw.kim
//
        System.out.println("########## token read test");
        System.out.println("id: "+info.get("id"));
        System.out.println("email: "+info.get("email"));
        System.out.println("penName: "+info.get("penName"));
        System.out.println("profile: "+info.get("profile"));
        System.out.println("lastDate: "+info.get("lastDate"));

        System.out.println("refreshToken: "+info.get("refreshToken"));
        System.out.println("refreshExpireDate: "+info.get("refreshExpireDate"));


        result.setSuccess(true);
        result.setMessage(Auth.OK);
        result.setResult(info);
        // TODO: 테스트용 메소드라서 사실상 쓸모 없음. 인터셉터로 기능 이전 바람.

        return result;
    }
}
