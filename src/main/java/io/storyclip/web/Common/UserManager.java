package io.storyclip.web.Common;

import io.storyclip.web.Entity.User;
import io.storyclip.web.Repository.UserRepository;
import io.storyclip.web.Encrypt.SHA256Util;
import org.springframework.stereotype.Component;

@Component
public class UserManager {

    // Autowired 대신 추천되는 의존성 주입 방식
    private static UserRepository UserRepo;
    public UserManager(UserRepository UserRepo) { this.UserRepo = UserRepo; }

    public static User getUserbyEmailAndPassword(String email, String password) {
        String salt = UserRepo.findSaltByEmail(email);
        SHA256Util sha256Util = new SHA256Util();
        return UserRepo.getUserByEmailAndPassword(email, sha256Util.encrypt(salt + password));
    }

    public static boolean emailCheck(String email) {
        Integer count = 1;
        if(email != null && email != "") {
            count = UserRepo.countByEmail(email);
        }
        return count <= 0 ? true : false;
    }
}
