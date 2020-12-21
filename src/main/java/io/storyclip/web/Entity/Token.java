package io.storyclip.web.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Table(name = "token")
@ToString(exclude = {"privateKey", "publicKey"})
@Entity
public class Token {

    @Id
    private String token;

    @Column(nullable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer userId;

    @Column(length = 100, nullable = false)
    @Convert(converter = AESCryptConverter.class) // 암호화
    @NotNull
    private String browser;

    @Column(length = 2048, nullable = false)
    @Convert(converter = AESCryptConverter.class) // 암호화
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String privateKey;

    @Column(length = 2048, nullable = false)
    @Convert(converter = AESCryptConverter.class) // 암호화
    @NotNull
    private String publicKey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = true, updatable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="Asia/Seoul")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date expireDate;

    @Column(length = 20, nullable = false)
    @Convert(converter = AESCryptConverter.class) // 암호화
    @NotNull
    private String refreshToken;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = true, updatable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="Asia/Seoul")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date refreshExpireDate;

}
