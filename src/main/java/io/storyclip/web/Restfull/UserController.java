package io.storyclip.web.Restfull;

import io.storyclip.web.Common.*;
import io.storyclip.web.Entity.Result;
import io.storyclip.web.Entity.User;
import io.storyclip.web.Exception.ParamRequiredException;
import io.storyclip.web.Repository.UserRepository;
import io.storyclip.web.Type.Auth;
import io.storyclip.web.Type.Type;
import io.storyclip.web.Encrypt.SHA256Util;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
@RequestMapping(value="/account")
public class UserController {
    
    // Autowired 대신 추천되는 의존성 주입 방식
    private static UserRepository UserRepo;
    public UserController(UserRepository UserRepo) {
        this.UserRepo = UserRepo;
    }

    @GetMapping(value="/signup-check/email")
    public Result emailCheck(@RequestParam(required = false) String email) {
        Result result = new Result();

        result.setSuccess(true);
        result.setMessage(Type.OK);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("usage", UserManager.emailCheck(email));

        result.setResult(hashMap);
        return result;
    }

    @PostMapping(value="/signup")
    public Result join(
        @RequestPart @RequestParam(required = false) MultipartFile profile,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String password,
        @RequestParam(required = false) String penName,
        @RequestParam(required = false) String recaptchaToken
    ) throws Exception {
        Result result = new Result();

        // validation 엔티티에 포함되지 않는 토큰이나 이런 것 때문에 @valid 어노테이션 사용안했음
        // hw.kim 2020-12-19 10:24
        if(email == null || password == null){
        	throw new ParamRequiredException(null);
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
        String salt = Common.createSecureRandom(32);

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

    // TODO: login에 RSA 키 발급 기능 추가: private key를 aes로 암호화해 넘겼다가 돌아올때 검증
    
    @GetMapping(value="/info")
    public Result signLook(@RequestHeader(value = "Authorization") String token) throws Exception {
    	Result result = new Result();
    	
		HashMap<String, Object> info = JWTManager.read(token);
		User user = UserRepo.findUserByUserId((Integer) info.get("id"));
		
		result.setSuccess(true);
		result.setMessage(Type.OK);
		result.setResult(user);
		
		return result;
	}
    
    @PatchMapping(value="/info")
    public Result signInfo(
		@RequestHeader(value = "Authorization") String token,
		@RequestPart @RequestParam(required = false) MultipartFile profile,
        @RequestParam(required = false) String penName
    ) throws Exception {
    	Result result = new Result();
    	
    	//토큰 아이디 확인
    	HashMap<String, Object> info = JWTManager.read(token);
		User user = UserRepo.findUserByUserId((Integer) info.get("id"));
		
		// 필명 업데이트
		if(penName != null){
			user.setPenName(penName);		
		};
		
		// 프로필 넣기
		if(profile != null) {
            String hash = FileManager.save(user.getUserId(), profile);
            user.setProfile(hash);
        }
		
		UserRepo.save(user);
		
		result.setSuccess(true);
	    result.setMessage(Type.OK);
	    
	    HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("update", true);
        result.setResult(hashMap);
	    
		return result;
    }
    
    @PatchMapping(value="/password")
    public Result newPasword(
		@RequestHeader(value = "Authorization") String token,
		@RequestBody HashMap<String, Object> param,
		HttpServletRequest request
    ) throws Exception {
    	Result result = new Result();
    	
    	HashMap<String, Object> info = JWTManager.read(token);
    	User user = UserRepo.findUserByUserId((Integer) info.get("id"));
    	
    	String password = (String) param.get("password");
    	String newPassword = (String) param.get("newPassword");
    	
    	if(password == null || newPassword == null) {
    		throw new ParamRequiredException(null);
    	}
    	
    	String salt = user.getSalt();
    	String email = user.getEmail();
    	String nowPassword = (SHA256Util.encrypt(salt + password));
    	User userinfo = UserRepo.getUserByEmailAndPassword(email, nowPassword);
    	if(userinfo == null) {
    		result.setSuccess(false);
            result.setMessage(Auth.PASSWORD_CHANGE_FAIL);
            return result;
    	}
    	
    	String newSalt = SHA256Util.createSalt(32);
    	userinfo.setSalt(newSalt);
    	userinfo.setPassword(SHA256Util.encrypt(newSalt + newPassword));
    	
    	UserRepo.save(userinfo);

    	result.setSuccess(true); 
    	result.setMessage(Type.OK); 
    	HashMap<String, Object> hashMap = new HashMap<>(); hashMap.put("update", true);
		result.setResult(hashMap);
		return result;
    }
}
