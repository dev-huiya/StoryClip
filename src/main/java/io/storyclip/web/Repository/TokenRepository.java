package io.storyclip.web.Repository;

import io.storyclip.web.Entity.Token;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface TokenRepository extends JpaRepository<Token, String> {

    public Token findTokenByRefreshToken(String refreshToken);

    @Cacheable("tokenKey")
    public Token getTokenByToken(String token);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM Token WHERE token = :token")
    @CacheEvict("tokenKey")
    public void deleteByToken(String token);
}
