package io.storyclip.web.Restfull;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.storyclip.web.Common.JWTManager;
import io.storyclip.web.Entity.Result;
import io.storyclip.web.Entity.Token;
import io.storyclip.web.Exception.RequiredAuthException;
import io.storyclip.web.Repository.TokenRepository;
import io.storyclip.web.Type.Auth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/auth")
public class AuthController {

    // Autowired 대신 추천되는 의존성 주입 방식
    private static TokenRepository TokenRepo;
    public AuthController(TokenRepository TokenRepo) {
        this.TokenRepo = TokenRepo;
    }

    @GetMapping(value="/verify")
    public Result verifyToken(HttpServletRequest request) throws Exception {
        Result result = new Result();

        String token = request.getHeader("Authorization");

        if(token == null) {
            throw new RequiredAuthException("Required token");
        }

        token = token.replace("Bearer ", "");

        DecodedJWT jwt = JWTManager.verify(token);

        result.setSuccess(true);
        result.setMessage(Auth.OK);
        result.setResult(jwt);
        // TODO: 테스트용 메소드라서 사실상 쓸모 없음. 인터셉터로 기능 이전 바람.

        return result;
    }

    // TODO: refresh_token 으로 access_token 재발급 API 개발 필요

    @PutMapping("/refresh")
    public Result refreshToken(HttpServletRequest request) throws Exception {
        Result result = new Result();

        String token = request.getHeader("Authorization");

        if(token == null) {
            throw new RequiredAuthException("Required token");
        }

        token = token.replace("Bearer ", "");

        TokenRepo.deleteByToken(token);

//        Token tokenObj = TokenRepo.getTokenByToken(token);
//        System.out.println("tokenObj"+tokenObj);
//        TokenRepo.delete(tokenObj);

        return result;
    }
}
