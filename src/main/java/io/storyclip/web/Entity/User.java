package io.storyclip.web.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

//@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Setter
// 빈 변수는 저장하거나 업데이트 하지 않도록 설정
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "user")
@ToString(exclude = "password")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Integer UserId;

    @Column(length = 100, nullable = false)
    @Convert(converter = AESCryptConverter.class) // 암호화
    private String email;

    @Column(length = 65, nullable = false)
    @JsonIgnore
    private String password;

    @Column(length = 20, nullable = false)
    @JsonIgnore
    private String salt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="Asia/Seoul")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true) // 다른 정보 업데이트 시 이 컬럼이 업데이트 되지 않도록 널 허용
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="Asia/Seoul")
    private Date lastDate;

    @Column(length = 255, nullable = true)
    private String profile;

    @Column(length = 255, nullable = true)
    private String kakaoAccountId;

    @Column(length = 4096, nullable = true)
    private String privateKey;
}
