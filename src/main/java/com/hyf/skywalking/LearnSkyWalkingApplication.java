package com.hyf.skywalking;

import com.hyf.skywalking.plugin.PluginComponent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * -Dskywalking.agent.service_name=test-application -Dskywalking.agent.is_open_debugging_class=true -Dskywalking.agent.is_cache_enhanced_class=true -Dskywalking.agent.class_cache_mode=FILE
 * 源码调试: -javaagent:E:\study\code\idea4\source-springcloud\component\skywalking\skywalking-agent\skywalking-agent.jar
 * <p>
 * 服务器:
 * -javaagent:E:\study\env\component\cloud-resources\apache-skywalking-apm-8.5.0\agent\skywalking-agent.jar -Dskywalking.agent.service_name=Research-Source
 * -javaagent:E:\study\env\component\cloud-resources\apache-skywalking-apm-es7-8.5.0\agent\skywalking-agent.jar -Dskywalking.agent.service_name=Research-Source
 */
@RestController
@SpringBootApplication
public class LearnSkyWalkingApplication {

    @Resource
    private PluginComponent pluginComponent;

    public static void main(String[] args) {
        // SW_OAL_ENGINE_DEBUG
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

    @RequestMapping("plugin")
    public String plugin() {
        pluginComponent.a();
        pluginComponent.b();
        return "";
    }
}
