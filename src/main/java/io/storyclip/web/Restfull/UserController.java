package io.storyclip.web.Restfull;

import io.storyclip.web.Entity.User;
import io.storyclip.web.Repository.UserRepository;
import io.storyclip.web.Utils.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    UserRepository UserRepo;

    @RequestMapping(value="/join", method= RequestMethod.POST)
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
    public User loginUser(String email, String password) {

        String salt = UserRepo.findUserByEmail(email);
        SHA256Util sha256Util = new SHA256Util();
        User user = UserRepo.getUserByEmailAndPassword(email, sha256Util.encrypt(salt + password));
        // 솔트 찾아서 해당 비밀번호로 조회
        // TODO: 이메일은 Entity에서 암호화하는데 비밀번호는 솔트값 때문에 밖에서 암호화 해서 넘겨줘야되는데 이거 너무 불편함.. 개선 바람.

        if(user == null) {
            return new User();
        }

        user.setLastDate(new Date());
        return UserRepo.save(user);
    }

}
