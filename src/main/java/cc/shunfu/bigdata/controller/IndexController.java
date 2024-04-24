package cc.shunfu.bigdata.controller;

import cc.shunfu.bigdata.model.result.ResponseData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-22
 */

@RestController
public class IndexController {

    @GetMapping("/")
    public ResponseData index() {
        return ResponseData.success("服务正常", null);
    }
}
