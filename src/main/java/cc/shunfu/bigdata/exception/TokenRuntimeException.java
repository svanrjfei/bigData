package cc.shunfu.bigdata.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-26
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class TokenRuntimeException extends RuntimeException{

    private Integer code = 401;
    private String msg;

    public TokenRuntimeException(String msg) {
        this.msg = msg;
    }

}
