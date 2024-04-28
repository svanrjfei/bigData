package cc.shunfu.bigdata.model.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseData {

    private boolean success;

    private int code;

    private String msg;

    private Object data;

    public static ResponseData success(String message, Object data) {
        return new ResponseData(true, 200, message, data);
    }

    public static ResponseData fail(int code, String msg) {
        return new ResponseData(false, code, msg, null);
    }

    public static ResponseData fail(String msg) {
        return new ResponseData(false, 500, msg, null);
    }

    public static ResponseData success() {
        return new ResponseData(true, 200, "请求成功", null);
    }
}
