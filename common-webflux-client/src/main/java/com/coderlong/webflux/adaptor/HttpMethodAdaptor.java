package com.coderlong.webflux.adaptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coderlong.webflux.beans.MethodInfo;
import com.google.common.base.Splitter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sailongren
 */
@Slf4j
public class HttpMethodAdaptor {
    private static final String HTTP_METHOD_POSTFIX = "Mapping";

    private static final String VALUE_FIELD = "value";

    private static final Splitter SPLITTER = Splitter.on(HTTP_METHOD_POSTFIX);

    private static final Set<Class> HTTP_METHODS = new HashSet<>();

    static {
        HTTP_METHODS.add(GetMapping.class);
        HTTP_METHODS.add(DeleteMapping.class);
        HTTP_METHODS.add(PostMapping.class);
        HTTP_METHODS.add(RequestMapping.class);
    }


    /**
     * 适配不同的HTTP方法
     */
    public static void adaptorHttpAnnotation(Annotation annotation, MethodInfo methodInfo) {
        HTTP_METHODS.stream()
                .filter(Objects::nonNull)
                .filter(method -> StringUtils.equalsIgnoreCase(annotation.annotationType().getSimpleName(), method.getSimpleName()))
                .forEach(method -> {
                    Method[] methods = annotation.getClass().getMethods();
                    String urlValue = "";
                    for (Method m1 : methods) {
                        if (StringUtils.equalsIgnoreCase(m1.getName(), VALUE_FIELD)) {
                            m1.setAccessible(true);
                            try {
                                urlValue = ((String[]) m1.invoke(annotation))[0];
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                log.error("非法访问", e);
                            }
                        }
                    }
                    if (StringUtils.isBlank(urlValue)) {
                        log.error("parse url value is blank");
                        throw new RuntimeException("parse url value is blank");
                    }
                    methodInfo.setUrl(urlValue);
                    methodInfo.setMethod(getHttpMethod(annotation, methodInfo));
                });
    }

    private static HttpMethod getHttpMethod(Annotation annotation, MethodInfo methodInfo) {
        if (annotation instanceof RequestMapping) {
            return HttpMethod.GET;
        }
        String methodPreFix = annotation.annotationType().getClass().getSimpleName();
        if (StringUtils.endsWith(methodPreFix, HTTP_METHOD_POSTFIX)) {
            return HttpMethod.valueOf(SPLITTER.splitToList(methodPreFix).get(0));
        }
        return null;
    }
}
