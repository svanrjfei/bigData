package cc.shunfu.bigdata.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author svanrj
 * @version 1.0
 * @CreateTime: 2024-04-28
 */

@Configuration
@ComponentScan(basePackages = {"cc.shunfu"})
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("舜富大数据中心").version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
