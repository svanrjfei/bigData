package cc.shunfu.bigdata.dto.vo.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {

    private boolean success;

    private int code;

    private String msg;

    private Object data;

    public static Response success(String message, Object data) {
        return new Response(true, 200, message, data);
    }

    public static Response fail(int code, String msg) {
        return new Response(false, code, msg, null);
    }

    public static Response fail(String msg) {
        return new Response(false, 500, msg, null);
    }

    public static Response success() {
        return new Response(true, 200, "请求成功", null);
    }
}
