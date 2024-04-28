package cc.shunfu.bigdata.interceptors;

import cc.shunfu.bigdata.exception.TokenRuntimeException;
import cc.shunfu.bigdata.utils.JWTUtils;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-26
 */

@Log4j2
@Component
public class JWTInterceptors implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 获取请求头中令牌
        String token = request.getHeader("Authorization");
        if (token != null) {
            token = token.substring(7);
        }
        try {
            // 验证令牌
            Map<String, Claim> claims = JWTUtils.verify(token);
            request.setAttribute("username", claims.get("username").toString());
            return true;  // 放行请求

        } catch (SignatureVerificationException e) {
            throw new TokenRuntimeException("无效签名！");
        } catch (TokenExpiredException e) {
            throw new TokenRuntimeException("token过期");
        } catch (AlgorithmMismatchException e) {
            throw new TokenRuntimeException("token算法不一致");
        } catch (Exception e) {
            throw new Exception("token为空");
        }
    }
}
