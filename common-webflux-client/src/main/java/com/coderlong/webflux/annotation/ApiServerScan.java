package com.coderlong.webflux.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.coderlong.webflux.config.WebFluxHttpAutoConfiguration;

/**
 * @author sailongren
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({WebFluxHttpAutoConfiguration.class})
public @interface ApiServerScan {

    /**
     * @see ApiServer 所注解的包扫描路径
     */
    String[] packages() default {};

}
