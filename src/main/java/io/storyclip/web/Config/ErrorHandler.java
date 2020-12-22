package io.storyclip.web.Config;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.storyclip.web.Entity.Result;
import io.storyclip.web.Exception.ForbiddenException;
import io.storyclip.web.Exception.RequiredAuthException;
import io.storyclip.web.Type.Auth;
import io.storyclip.web.Type.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestControllerAdvice
public class ErrorHandler {

    // 403 로그인은 되었지만 해당 컨텐츠에 권한 없음 오류
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Result> Forbidden() {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(Http.FORBIDDEN);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }

    // 404 Http Error
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Result> NotFound() {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(Http.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    // 401 토큰 없음 오류
    @ExceptionHandler(RequiredAuthException.class)
    public ResponseEntity<Result> RequiredAuth() {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(Auth.AUTH_REQURED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    // 토큰 만료 오류
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Result> TokenExpired() {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(Auth.JWT_EXPIRED_ERROR);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    // 알고리즘을 찾을 수 없음. 사실상 발생하지 않을 예정일 오류
    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<Result> NoSuchAlgorithm() {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(Auth.JWT_ALGORITHM_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    // 디비에 저장된 키와 토큰에 실제 사용된 키가 불일치 오류
    @ExceptionHandler({InvalidKeySpecException.class, SignatureVerificationException.class})
    public ResponseEntity<Result> JWTKeyError() {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(Auth.JWT_KEY_EMPTY);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    // Claim 오류. (ex. 발급자가 일치하지 않음)
    @ExceptionHandler(InvalidClaimException.class)
    public ResponseEntity<Result> InvalidClaim() {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(Auth.JWT_INVALID_CLAIM);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    // 그외 다양한 토큰 오류
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<Result> JWTVerification() {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(Auth.JWT_VERIFY_ERROR);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

}
