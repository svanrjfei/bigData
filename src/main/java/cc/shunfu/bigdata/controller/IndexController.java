package cc.shunfu.bigdata.controller;

import cc.shunfu.bigdata.dto.entity.UserDetails;
import cc.shunfu.bigdata.dto.vo.result.Response;
import cc.shunfu.bigdata.utils.JWTUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-22
 */

@RestController
@Log4j2

@Tag(name = "测试Controller", description = "这是描述")
public class IndexController {

    @GetMapping("/")
    @Operation(summary = "测试接口")
    public Response index() {
        log.info("服务正常");
        return Response.success("服务正常", null);
    }

    @PostMapping("/getToken")
    @Operation(summary = "获取token")
    public Response getToken(@RequestBody(required = false) UserDetails user) {
        if (user == null) {
            return Response.fail(400, "username不可以为空");
        }
        String token = JWTUtils.getToken(user);

        JSONObject jsonObject = new JSONObject();
        // 添加键值对
        jsonObject.put("token", token);

        return Response.success("服务正常", jsonObject);
    }

    @PostMapping("/verifyToken")
    @Operation(summary = "验证token")
    public Response verifyToken(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return Response.success("服务正常", username);
    }
}
