package com.hyf.skywalking.advanced;

/**
 * @author baB_hyf
 * @date 2021/06/13
 */
public class IgnoreException {

    // In some codes, the exception is being used as a way of controlling business flow.
    // when an exception encounter in tracing, skywalking will return false instead of throw ex.

    // 1. set statuscheck.ignored_exceptions in agent.config. final span status is false, not true.
    // 2. add @IgnoreException in exception class. this annotation is in apm-toolkit-log4j-1.x.


    // Recursive check
    // set statuscheck.max_recursive_depth in agent.config to unwrap exception count.
    // not recommend to set greater than 10.
}
