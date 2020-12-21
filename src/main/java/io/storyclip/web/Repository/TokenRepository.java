package io.storyclip.web.Repository;

import io.storyclip.web.Entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {

    public Token findTokenByRefreshToken(String refreshToken);

    public Token getTokenByToken(String token);
}
