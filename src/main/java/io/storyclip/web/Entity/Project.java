package io.storyclip.web.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
// 빈 변수는 저장하거나 업데이트 하지 않도록 설정
@DynamicInsert
@DynamicUpdate
@Table(name = "project")
@ToString
@Data
@Entity
public class Project {

    @Id
    private String projectId;

    @Column(nullable = false)
    @NotNull
    private Integer userId;

    @Column(length = 255)
    @Convert(converter = AESCryptConverter.class) // 암호화
    private String title;

    @Column(length = 255)
    @Convert(converter = AESCryptConverter.class) // 암호화
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false, updatable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="Asia/Seoul")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true) // 다른 정보 업데이트 시 이 컬럼이 업데이트 되지 않도록 널 허용
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS", timezone="Asia/Seoul")
    private Date modifyDate;
}
