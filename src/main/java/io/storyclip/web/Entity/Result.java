package io.storyclip.web.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Result {
    private Boolean success;
    private Enum message;
    private Object resultData;

    public void setResult(Object resultData) {
        this.resultData = resultData;
    }
}
