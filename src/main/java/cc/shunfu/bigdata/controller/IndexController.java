package cc.shunfu.bigdata.controller;

import cc.shunfu.bigdata.model.entity.UserDetails;
import cc.shunfu.bigdata.model.result.ResponseData;
import cc.shunfu.bigdata.utils.JWTUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-22
 */

@RestController
@Log4j2
public class IndexController {

    @GetMapping("/")
    public ResponseData index() {
        log.info("服务正常");
        return ResponseData.success("服务正常", null);
    }

    @PostMapping("/getToken")
    public ResponseData getToken() {
        UserDetails user = new UserDetails();
        user.setUsername("admin");
        user.setPassword("123456");
        String token = JWTUtils.getToken(user);

        JSONObject jsonObject = new JSONObject();
        // 添加键值对
        jsonObject.put("token", token);

        return ResponseData.success("服务正常", jsonObject);
    }

    @PostMapping("/verifyToken")
    public ResponseData verifyToken(@RequestParam("token") String token, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return ResponseData.success("服务正常", username);
    }
}
