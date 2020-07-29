package com.coderlong.webflux.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Http服务相关信息
 * @author sailongren
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiServer {
    /**
     *
     * @return
     */
    String value() default "";

    /**
     *  服务提供者名字
     * @return
     */
    String microName() default "";
}
