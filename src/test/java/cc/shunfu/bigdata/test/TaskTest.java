package cc.shunfu.bigdata.test;


import org.junit.jupiter.api.Test;

import java.net.URL;

public class TaskTest {

    @Test
    public void test() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("logback-spring.xml");
        if (url == null) {
            System.out.println("logback.xml配置文件不存在");
        } else {
            System.out.println("logback.xml配置文件路径：" + url.getPath());
        }

    }
}