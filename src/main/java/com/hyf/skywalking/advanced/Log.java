package com.hyf.skywalking.advanced;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author baB_hyf
 * @date 2021/06/13
 */
// @RestController
@RequestMapping("log")
public class Log {

    @RequestMapping("log4j")
    public String log4j() {

        Logger log4j = LogManager.getLogger(Log.class);
        log4j.info("this is log4j print");

        return "log4j";
    }

    @RequestMapping("log4j2")
    public String log4j2() {

        Logger log4j = LogManager.getLogger(Log.class);
        log4j.info("this is log4j2 print");

        return "log4j2";
    }

    @RequestMapping("logback")
    public String logback() {

        Logger log4j = LogManager.getLogger(Log.class);
        log4j.info("this is log4j print");

        return "logback";
    }
}
