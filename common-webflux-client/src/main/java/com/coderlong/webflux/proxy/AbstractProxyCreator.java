package com.coderlong.webflux.proxy;

import static com.coderlong.webflux.adaptor.HttpMethodAdaptor.adaptorHttpAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.coderlong.webflux.beans.MethodInfo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author sailongren
 */
@Slf4j
abstract class AbstractProxyCreator implements ProxyCreator{

    /**
     * 提取返回对象信息
     */
    protected void extractReturnInfo(Method method,
            MethodInfo methodInfo) {
        // 返回flux还是mono
        // isAssignableFrom 判断类型是否某个的子类
        // instanceof 判断实例是否某个的子类
        boolean isFlux = method.getReturnType()
                .isAssignableFrom(Flux.class);
        methodInfo.setReturnFlux(isFlux);

        // 得到返回对象的实际类型
        Class<?> elementType = extractElementType(method.getGenericReturnType());
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
    protected void extractRequestParamAndBody(Method method,
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
                methodInfo.setBodyElementType(extractElementType(parameters[i].getParameterizedType()));
            }
        }
    }

    /**
     * 得到请求的URL和方法
     */
    protected void extractUrlAndMethod(Method method,
            MethodInfo methodInfo) {
        Annotation[] annotations = method.getAnnotations();

        for (Annotation annotation : annotations) {
            adaptorHttpAnnotation(annotation, methodInfo);
        }
    }

}
