package cc.shunfu.bigdata.exception;

import cc.shunfu.bigdata.model.result.ErrorCode;
import cc.shunfu.bigdata.model.result.ResponseData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-28
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /*
    全局异常处理，没有指定异常的类型，不管什么异常均可以捕获
     */
    @ExceptionHandler(Exception.class)
    /*如果不加，则会导致无法进行异常处理*/
    @ResponseBody
    public ResponseData error(Exception e) {
        e.printStackTrace();
        return ResponseData.fail("系统错误！");
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public ResponseData error(ArithmeticException e) {
        e.printStackTrace();
        return ResponseData.fail(ErrorCode.ARITHMETIC_EXCEPTION.getCode(), ErrorCode.ARITHMETIC_EXCEPTION.getMsg());
    }

    //自定义异常类
    @ExceptionHandler(TokenRuntimeException.class)
    @ResponseBody
    public ResponseData error(TokenRuntimeException e) {
        e.printStackTrace();
        return ResponseData.fail(e.getCode(), e.getMsg());
    }
}
