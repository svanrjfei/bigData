package cc.shunfu.bigdata.dto.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-26
 */

@Data
@Schema(name = "用户信息类", description = "用户信息")
public class UserDetails {

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "姓名")
    private String fullName;

    @Schema(description = "年龄")
    private int age;
}
