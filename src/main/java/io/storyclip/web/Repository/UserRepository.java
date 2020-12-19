package io.storyclip.web.Repository;

import io.storyclip.web.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value="SELECT salt FROM User WHERE email = :email")
    public String findSaltByEmail(String email);

    public Integer countByEmail(String email);

    public User getUserByEmailAndPassword(String email, String saltedPassword);
}
