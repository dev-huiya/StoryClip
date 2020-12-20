package io.storyclip.web.Restfull;

import io.storyclip.web.Common.FileManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/images")
public class FileController {

    @RequestMapping(value="/{hash}")
    public ResponseEntity<byte[]> getImage(@PathVariable String hash){
        // TODO: JWT 검증을 통해 userId 가져와야 함.
        // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        FileManager fileManager = new FileManager();
        byte[] file = fileManager.get(14, hash);

        if(file == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }
    
    // TODO: 이미지 업로드 API 개발 필요. 단, 로그인 기능 이후
}
