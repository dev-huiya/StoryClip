package io.storyclip.web.Common;

import io.storyclip.web.Entity.User;
import io.storyclip.web.Repository.UserRepository;
import io.storyclip.web.Utils.SHA256Util;

public class Entity {

    // Autowired 대신 추천되는 의존성 주입 방식
    private static UserRepository UserRepo;
    public Entity(UserRepository UserRepo) {
        this.UserRepo = UserRepo;
    }

    public User getUserbyEmailAndPassword(String email, String password) {
        String salt = UserRepo.findSaltByEmail(email);
        SHA256Util sha256Util = new SHA256Util();
        return UserRepo.getUserByEmailAndPassword(email, sha256Util.encrypt(salt + password));
    }

    public boolean emailCheck(String email) {
        Integer count = 1;
        if(email != null && email != "") {
            count = UserRepo.countByEmail(email);
        }
        return count <= 0 ? true : false;
    }
}
