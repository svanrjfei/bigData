package cc.shunfu.bigdata.model.entity;

import lombok.Data;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-26
 */

@Data
public class UserDetails {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private int age;
}
