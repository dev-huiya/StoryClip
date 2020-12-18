package io.storyclip.web.Restfull;

import io.storyclip.web.Common.Entity;
import io.storyclip.web.Entity.Result;
import io.storyclip.web.Entity.User;
import io.storyclip.web.Repository.UserRepository;
import io.storyclip.web.Type.Auth;
import io.storyclip.web.Type.Type;
import io.storyclip.web.Utils.SHA256Util;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

@CrossOrigin //(origins="http://localhost")
@RestController
@RequestMapping(value="/api/user")
public class UserController {
    
    // Autowired 대신 추천되는 의존성 주입 방식
    private static UserRepository UserRepo;
    public UserController(UserRepository UserRepo) {
        this.UserRepo = UserRepo;
    }

    @RequestMapping(value="/join", method= RequestMethod.POST)
    public Result join(
            @RequestPart @RequestParam(required = false) MultipartFile profile,
            @Valid User RequestUser, BindingResult bindingResult
    ) {
        Result result = new Result();

        if(bindingResult.hasErrors()){
            result.setSuccess(false);
            result.setMessage(Auth.AUTH_WRONG);
            return result;
        }

        result.setSuccess(true);
        result.setMessage(Type.OK);

        return result;
    }

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
