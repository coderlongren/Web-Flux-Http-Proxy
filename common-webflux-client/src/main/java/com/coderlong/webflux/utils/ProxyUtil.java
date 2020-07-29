package com.coderlong.webflux.utils;

import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringUtils;

import com.coderlong.webflux.proxy.JDKProxyCreator;
import com.coderlong.webflux.proxy.ProxyCreator;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sailongren
 */
@Slf4j
public class ProxyUtil {
    public static void registerBeans(BeanFactory beanFactory, Set<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String className = beanDefinition.getBeanClassName();
            if (StringUtils.isEmpty(className)) {
                continue;
            }
            try {
                // 创建代理类
                Class<?> target = Class.forName(className);
                Object invoker = new Object();
                ProxyCreator proxyCreator = new JDKProxyCreator();
                Object proxy = proxyCreator.createProxy(target);
                //                    InvocationHandler invocationHandler = new GrpcServiceProxy<>(target, invoker);
                //                    Object proxy = Proxy.newProxyInstance(GrpcService.class.getClassLoader(), new
                //                    Class[]{target}, invocationHandler);

                // 注册到 Spring 容器
                String beanName = ClassNameUtils.beanName(className);
                ((DefaultListableBeanFactory) beanFactory).registerSingleton(beanName, proxy);
            } catch (ClassNotFoundException e) {
                log.warn("class not found : " + className);
            }
        }
    }
}
