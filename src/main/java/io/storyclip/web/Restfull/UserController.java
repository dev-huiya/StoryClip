package io.storyclip.web.Restfull;

import com.google.gson.JsonObject;
import io.storyclip.web.Common.Entity;
import io.storyclip.web.Common.FileManager;
import io.storyclip.web.Common.Recaptcha;
import io.storyclip.web.Entity.Result;
import io.storyclip.web.Entity.User;
import io.storyclip.web.Repository.UserRepository;
import io.storyclip.web.Type.Auth;
import io.storyclip.web.Type.Type;
import io.storyclip.web.Utils.AES256Util;
import io.storyclip.web.Utils.SHA256Util;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

@CrossOrigin //(origins="http://localhost")
@RestController
@RequestMapping(value="/api")
public class UserController {
    
    // Autowired 대신 추천되는 의존성 주입 방식
    private static UserRepository UserRepo;
    public UserController(UserRepository UserRepo) {
        this.UserRepo = UserRepo;
    }

    @RequestMapping(value="/signup-check/email", method= RequestMethod.GET)
    public Result emailCheck(@RequestParam(required = false) String email) {
        Result result = new Result();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        Entity entity = new Entity(UserRepo);

        result.setSuccess(true);
        result.setMessage(Type.OK);

        hashMap.put("usage", emailCheck(email));
        result.setResult(hashMap);
        return result;
    }

    @RequestMapping(value="/user/join", method= RequestMethod.POST)
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
        Entity entity = new Entity(UserRepo);
        if(!entity.emailCheck(email)) {
            result.setSuccess(false);
            result.setMessage(Auth.JOIN_DUPLICATE);
            return result;
        }
    
        // 솔트 넣는건 수동임
        SHA256Util sha256Util = new SHA256Util();
        String salt = sha256Util.getSalt();

        // 유저 생성
        User user = new User();
        user.setEmail(email);
        user.setPenName(penName);
        user.setPassword(sha256Util.encrypt(salt + password));
        user.setSalt(salt);
        user = UserRepo.save(user);

        // 프로필 저장
        if(profile != null) {
            FileManager fileManager = new FileManager();
            String hash = fileManager.save(user.getUserId(), profile);
            user.setProfile(hash);
            UserRepo.save(user);
        }

        // TODO: 이미지 뷰 페이지 (JWT 이후)
        // TODO: 이미지 복호화 테스트

        result.setSuccess(true);
        result.setMessage(Type.OK);

        return result;
    }

    // ############################# 이 밑은 테스트 코드

    @RequestMapping(value="/join2", method= RequestMethod.POST)
    public User joinUser(String email, String password) {
        User user = new User();
        user.setEmail(email); // Entity 내부에서 암호화

        SHA256Util sha256Util = new SHA256Util();
        String salt = sha256Util.getSalt();

        user.setPassword(sha256Util.encrypt(salt + password));
        user.setSalt(salt);
        return UserRepo.save(user);
    }

    @RequestMapping(value="/edit", method= RequestMethod.POST)
    public User editUser(Integer userId, String email, String password, String salt) {
        User user = UserRepo.getOne(userId);
        user.setEmail(email);
        user.setPassword(password);
        user.setSalt(salt);
        return UserRepo.save(user);
    }

    @RequestMapping(value="/info", method= RequestMethod.POST)
    public Result userInfo(@Valid @RequestBody User RequestUser, BindingResult bindingResult) {
        Result result = new Result();

        if (bindingResult.hasErrors()) {
            result.setSuccess(false);
            result.setMessage(Auth.AUTH_WRONG);
            return result;
        }

        result.setSuccess(true);
        result.setMessage(Auth.OK);

        Entity entity = new Entity(UserRepo);
        User user = entity.getUserbyEmailAndPassword(RequestUser.getEmail(), RequestUser.getPassword());

        result.setResult(user);

        return result;
    }

    @RequestMapping(value="/login", method= RequestMethod.POST)
    public Result loginUser(@Valid @RequestBody User RequestUser, BindingResult bindingResult) {
        Result result = new Result();

        if(bindingResult.hasErrors()){
            result.setSuccess(false);
            result.setMessage(Auth.AUTH_WRONG);
            return result;
        }

        Entity entity = new Entity(UserRepo);
        User user = entity.getUserbyEmailAndPassword(RequestUser.getEmail(), RequestUser.getPassword());
        // 솔트 찾아서 해당 비밀번호로 조회

        if(user == null) {
            result.setSuccess(false);
            result.setMessage(Auth.AUTH_WRONG);
            return result;
        }

        user.setLastDate(new Date());

        result.setSuccess(true);
        result.setMessage(Auth.OK);
        result.setResult(UserRepo.save(user));

        return result;
    }

    // TODO: intersepter로 JWT 검증 단계 추가
    // TODO: login에 RSA 키 발급 기능 추가: private key를 aes로 암호화해 넘겼다가 돌아올때 검증

    @RequestMapping(value="/test")
    public Result test() {
        Result result = new Result();

        result.setSuccess(true);
        result.setMessage(Type.OK);

        Properties props = System.getProperties();
        for(Enumeration en = props.propertyNames(); en.hasMoreElements();) {
            String key = (String)en.nextElement();
            String value = props.getProperty(key);
            System.out.println(key + "=" + value);
        }


        return result;
    }

}
