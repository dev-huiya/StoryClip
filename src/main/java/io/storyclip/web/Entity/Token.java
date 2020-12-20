package io.storyclip.web.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Table(name = "token")
@ToString(exclude = {"privateKey", "publicKey"})
@Entity
public class Token {

    @Id
    private String token;

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


}
