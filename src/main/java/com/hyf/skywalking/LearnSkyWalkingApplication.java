package com.hyf.skywalking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 源码调试: -javaagent:E:\study\idea4\source-springcloud\component\skywalking\skywalking-agent\skywalking-agent.jar
 * <p>
 * 服务器:
 * -javaagent:E:\study\idea4\source-springcloud\cloud-resources\skywalking\apache-skywalking-apm-8.5.0\agent\skywalking-agent.jar -Dskywalking.agent.service_name=Research-Source
 * -javaagent:E:\study\idea4\source-springcloud\cloud-resources\skywalking\apache-skywalking-apm-es7-8.5.0\agent\skywalking-agent.jar -Dskywalking.agent.service_name=Research-Source
 */
@RestController
@SpringBootApplication
public class LearnSkyWalkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnSkyWalkingApplication.class, args);
    }

    @RequestMapping
    public String home() {
        return "home";
    }

    @RequestMapping("ex")
    public void ex() {
        throw new RuntimeException();
    }

    @RequestMapping("req")
    public String req() {
        return new RestTemplate().getForObject("https://www.baidu.com", String.class);
    }
}
