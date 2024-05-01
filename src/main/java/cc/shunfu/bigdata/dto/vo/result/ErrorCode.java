package cc.shunfu.bigdata.dto.vo.result;

import lombok.Getter;

@Getter
public enum ErrorCode {

    PARAMS_ERROR(10001, "参数有误"),
    ACCOUNT_PWD_NOT_EXIST(10002, "用户名或密码不存在"),
    NO_PERMISSION(70001, "无访问权限"),
    SESSION_TIME_OUT(90001, "会话超时"),
    NO_LOGIN(90002, "未登录"),
    ARITHMETIC_EXCEPTION(2010, "算数异常"),
    ;

    private final int code;
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}