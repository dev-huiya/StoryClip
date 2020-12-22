package io.storyclip.web.Config;

import io.storyclip.web.Common.JWTManager;
import io.storyclip.web.Exception.AuthRequiredException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthInterceptor implements HandlerInterceptor {

    private String HEADER_TOKEN_KEY = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        if(token == null) {
            throw new AuthRequiredException("Required token");
        }
        token = token.replace(HEADER_TOKEN_KEY, "");
        JWTManager.verify(token);

        return true;
    }
}