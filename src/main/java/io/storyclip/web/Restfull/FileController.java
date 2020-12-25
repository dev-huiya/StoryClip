package io.storyclip.web.Restfull;

import io.storyclip.web.Common.FileManager;
import io.storyclip.web.Common.JWTManager;
import io.storyclip.web.Exception.ForbiddenException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(value="/")
public class FileController {

    @Cacheable("images")
    @RequestMapping(value={"/{hash}","/images/{hash}"})
    public ResponseEntity<byte[]> getImage(@RequestHeader("Authorization") String token, @PathVariable String hash) throws Exception {

        HashMap<String, Object> info = JWTManager.read(token);
        byte[] file = FileManager.get((Integer) info.get("id") , hash);

        if(file == null) {
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            throw new ForbiddenException("Forbidden");
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setCacheControl("must-revalidate");
        headers.setAccessControlMaxAge(24 * 60 * 1000);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }
    
    // TODO: 이미지 업로드 API 개발 필요. 단, 로그인 기능 이후
}
