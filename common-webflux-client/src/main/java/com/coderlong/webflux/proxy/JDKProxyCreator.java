package com.coderlong.webflux.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.coderlong.webflux.annotation.ApiServer;
import com.coderlong.webflux.beans.MethodInfo;
import com.coderlong.webflux.beans.ServerInfo;
import com.coderlong.webflux.handle.RestHandle;
import com.coderlong.webflux.handle.WebClientRestHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用jdk动态代理实现代理类
 * @author sailongren
 */
@Slf4j
public class JDKProxyCreator extends AbstractProxyCreator {

    @Override
    public Object createProxy(Class<?> type) {
        log.info("createProxy: {}", type);
        // 根据接口得到API服务器信息
        ServerInfo serverInfo = extractServerInfo(type);
        log.info("serverInfo: microName = {}", serverInfo.getMicroName());
        // 给每一个代理类一个实现
        RestHandle handler = new WebClientRestHandler();

        // 初始化服务器信息(初始化webclient)
        handler.init(serverInfo);
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {type}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method,
                    Object[] args) {
                // 根据方法和参数得到调用信息
                MethodInfo methodInfo = extractMethodInfo(method, args);
                log.info("methodInfo: {}", methodInfo);
                // 调用rest
                return handler.invokeRest(methodInfo, serverInfo);
            }
        });
    }

    /**
     * 根据方法定义和调用参数得到调用的相关信息
     */
    private MethodInfo extractMethodInfo(Method method,
            Object[] args) {
        MethodInfo methodInfo = new MethodInfo();
        extractUrlAndMethod(method, methodInfo);
        extractRequestParamAndBody(method, args, methodInfo);
        // 提取返回对象信息
        extractReturnInfo(method, methodInfo);
        return methodInfo;
    }

    /**
     * 提取服务器信息
     */
    private ServerInfo extractServerInfo(Class<?> type) {
        ServerInfo serverInfo = new ServerInfo();
        ApiServer apiAnno = type.getAnnotation(ApiServer.class);
        serverInfo.setUrl(apiAnno.value());
        serverInfo.setMicroName(apiAnno.microName());
        return serverInfo;
    }
}
