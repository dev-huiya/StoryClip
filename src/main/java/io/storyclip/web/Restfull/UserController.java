package io.storyclip.web.Restfull;

import io.storyclip.web.Common.*;
import io.storyclip.web.Entity.Result;
import io.storyclip.web.Entity.Token;
import io.storyclip.web.Entity.User;
import io.storyclip.web.Repository.UserRepository;
import io.storyclip.web.Type.Auth;
import io.storyclip.web.Type.Type;
import io.storyclip.web.Encrypt.SHA256Util;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping(value="/account")
public class UserController {
    
    // Autowired 대신 추천되는 의존성 주입 방식
    private static UserRepository UserRepo;
    public UserController(UserRepository UserRepo) {
        this.UserRepo = UserRepo;
    }

    @RequestMapping(value="/signup-check/email", method= RequestMethod.GET)
    public Result emailCheck(@RequestParam(required = false) String email) {
        Result result = new Result();

        result.setSuccess(true);
        result.setMessage(Type.OK);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("usage", UserManager.emailCheck(email));

        result.setResult(hashMap);
        return result;
    }

    @RequestMapping(value="/signup", method= RequestMethod.POST)
    public Result join(
        @RequestPart @RequestParam(required = false) MultipartFile profile,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String password,
        @RequestParam(required = false) String penName,
        @RequestParam(required = false) String recaptchaToken
    ) {
        Result result = new Result();

        // validation 엔티티에 포함되지 않는 토큰이나 이런 것 때문에 @valid 어노테이션 사용안했음
        // hw.kim 2020-12-19 10:24
        if(email == null || password == null){
            result.setSuccess(false);
            result.setMessage(Auth.AUTH_WRONG);
            return result;
        }

        if(recaptchaToken == null) {
            result.setSuccess(false);
            result.setMessage(Auth.CAPTCHA_EMPTY);
            return result;
        }

        // 리캡챠 검증
        Recaptcha recaptcha = new Recaptcha();
        if(!recaptcha.verify(recaptchaToken)){
            result.setSuccess(false);
            result.setMessage(Auth.CAPTCHA_FAIL);
            return result;
        }

        // 이메일 중복 검증
        if(!UserManager.emailCheck(email)) {
            result.setSuccess(false);
            result.setMessage(Auth.JOIN_DUPLICATE);
            return result;
        }
    
        // 솔트 넣는건 수동임
        String salt = SHA256Util.getSalt(32);

        // 유저 생성
        User user = new User();
        user.setEmail(email);
        user.setPenName(penName);
        user.setPassword(SHA256Util.encrypt(salt + password));
        user.setSalt(salt);
        user = UserRepo.save(user);

        // 프로필 저장
        if(profile != null) {
            String hash = FileManager.save(user.getUserId(), profile);
            user.setProfile(hash);
            UserRepo.save(user);
        }

        result.setSuccess(true);
        result.setMessage(Type.OK);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("join", true);
        result.setResult(hashMap);

        return result;
    }

    @RequestMapping(value="/signin", method= RequestMethod.POST)
    public Result login(@Valid @RequestBody User RequestUser, BindingResult bindingResult, HttpServletRequest request) {
        Result result = new Result();

        if(bindingResult.hasErrors()){
            result.setSuccess(false);
            result.setMessage(Auth.AUTH_WRONG);
            return result;
        }

        User user = UserManager.getUserbyEmailAndPassword(RequestUser.getEmail(), RequestUser.getPassword());
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

        Token token = JWTManager.create(user, UserAgentParser.getUserAgent(request), true);

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

    // TODO: login에 RSA 키 발급 기능 추가: private key를 aes로 암호화해 넘겼다가 돌아올때 검증

    // ############################# 이 밑은 테스트 코드

    @RequestMapping(value="/test")
    public Result test(HttpServletRequest req) {
        Result result = new Result();

        result.setSuccess(true);
        result.setMessage(Type.OK);

        result.setResult(UserAgentParser.getUserAgent(req));
        return result;
    }

}
