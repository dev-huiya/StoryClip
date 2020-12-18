package io.storyclip.web.Entity;

import io.storyclip.web.Type.Type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Result {
    private Boolean success = false;
    private Enum message = Type.RESULT_NOT_SET;
    private Object resultData = null;

    public void setResult(Object resultData) {
        this.resultData = resultData;
    }
}
