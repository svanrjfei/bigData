package cc.shunfu.bigdata.utils;

import cc.shunfu.bigdata.dto.entity.UserDetails;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.log4j.Log4j2;

import java.util.Calendar;
import java.util.Map;

@Log4j2
public class JWTUtils {


    private static final String SING = "XIAOSHUANG";


    public static String getToken(UserDetails user) {

        Calendar instance = Calendar.getInstance();
        // 默认7天过期
        instance.add(Calendar.DATE, 7);

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("username", user.getUsername());

        return builder.withExpiresAt(instance.getTime())  //指定令牌过期时间
                .sign(Algorithm.HMAC256(SING));
    }

    /**
     * 验证token  合法性
     */
    public static Map<String, Claim> verify(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token).getClaims();
    }

}
