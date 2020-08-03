package com.coderlong.webflux.proxy;


import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coderlong.webflux.annotation.ApiServer;
import com.coderlong.webflux.beans.MethodInfo;
import com.coderlong.webflux.beans.ServerInfo;
import com.coderlong.webflux.handle.RestHandle;
import com.coderlong.webflux.handle.WebClientRestHandler;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 使用jdk动态代理实现代理类
 */
@Slf4j
public class JDKProxyCreator implements ProxyCreator {

    @Override
    public Object createProxy(Class<?> type) {
        log.info("createProxy:" + type);
        // 根据接口得到API服务器信息
        ServerInfo serverInfo = extractServerInfo(type);
        log.info("serverInfo:" + serverInfo);
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
                log.info("methodInfo:" + methodInfo);
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
     * 提取返回对象信息
     */
    private void extractReturnInfo(Method method,
            MethodInfo methodInfo) {
        // 返回flux还是mono
        // isAssignableFrom 判断类型是否某个的子类
        // instanceof 判断实例是否某个的子类
        boolean isFlux = method.getReturnType()
                .isAssignableFrom(Flux.class);
        methodInfo.setReturnFlux(isFlux);

        // 得到返回对象的实际类型
        Class<?> elementType = extractElementType(
                method.getGenericReturnType());
        methodInfo.setReturnElementType(elementType);
    }

    /**
     * 得到泛型类型的实际类型
     */
    private Class<?> extractElementType(
            Type genericReturnType) {
        Type[] actualTypeArguments = ((ParameterizedType) genericReturnType)
                .getActualTypeArguments();

        return (Class<?>) actualTypeArguments[0];
    }

    /**
     * 得到请求的param和body
     */
    private void extractRequestParamAndBody(Method method,
            Object[] args, MethodInfo methodInfo) {
        // 得到调用的参数和body
        Parameter[] parameters = method.getParameters();

        // 参数和值对应的map
        Map<String, Object> params = new LinkedHashMap<>();
        methodInfo.setParams(params);

        for (int i = 0; i < parameters.length; i++) {
            // 是否带 @PathVariable
            PathVariable annoPath = parameters[i]
                    .getAnnotation(PathVariable.class);

            if (annoPath != null) {
                params.put(annoPath.value(), args[i]);
            }

            // 是否带了 RequestBody
            RequestBody annoBody = parameters[i]
                    .getAnnotation(RequestBody.class);

            if (annoBody != null) {
                methodInfo.setBody((Mono<?>) args[i]);
                // 请求对象的实际类型
                methodInfo.setBodyElementType(
                        extractElementType(parameters[i]
                                .getParameterizedType()));
            }
        }
    }

    /**
     * 得到请求的URL和方法
     */
    private void extractUrlAndMethod(Method method,
            MethodInfo methodInfo) {
        // 得到请求URL和请求方法
        Annotation[] annotations = method.getAnnotations();

        for (Annotation annotation : annotations) {
            // GET
            if (annotation instanceof GetMapping) {
                GetMapping a = (GetMapping) annotation;

                methodInfo.setUrl(a.value()[0]);
                methodInfo.setMethod(HttpMethod.GET);
            }
            // POST
            else if (annotation instanceof PostMapping) {
                PostMapping a = (PostMapping) annotation;

                methodInfo.setUrl(a.value()[0]);
                methodInfo.setMethod(HttpMethod.POST);
            }
            // DELETE
            else if (annotation instanceof DeleteMapping) {
                DeleteMapping a = (DeleteMapping) annotation;

                methodInfo.setUrl(a.value()[0]);
                methodInfo.setMethod(HttpMethod.DELETE);
            }
            // request mapping
            else if (annotation instanceof RequestMapping) {
                RequestMapping methodMapping = (RequestMapping) annotation;
                RequestMethod[] methods = methodMapping.method();
                if (methods.length == 0) {
                    methods = new RequestMethod[] {RequestMethod.GET};
                }
                methodInfo.setMethod(HttpMethod.valueOf(methods[0].name()));
                if (methodMapping.value().length > 0) {
                    if (!StringUtils.isEmpty(methodMapping.value()[0])) {
                        methodInfo.setUrl(methodMapping.value()[0]);
                    }
                }
            }
        }
    }

    /**
     * 提取服务器信息
     */
    private ServerInfo extractServerInfo(Class<?> type) {
        ServerInfo serverInfo = new ServerInfo();
        ApiServer anno = type.getAnnotation(ApiServer.class);
        serverInfo.setUrl(anno.value());
        serverInfo.setMicroName(anno.microName());
        return serverInfo;
    }
}
