package com.coderlong.webflux.handle;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.coderlong.webflux.beans.MethodInfo;
import com.coderlong.webflux.beans.ServerInfo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * rest webClient 处理
 *
 * @author sailongren
 */
@Slf4j
public class WebClientRestHandler implements RestHandle<MethodInfo, ServerInfo> {

    private WebClient client;
    private RequestBodySpec request;

    @Override
    public void init(ServerInfo serverInfo) {
        log.info("WebClientRestHandler start init.....");
        //获取负载均衡的 webClient
//        WebClient.Builder bean = SpringContextHolder.getBean(WebClient.Builder.class);
        this.client = WebClient.create(serverInfo.getUrl());
        log.info("WebClientRestHandler started success");
    }

    /**
     * 处理rest请求
     */
    @Override
    public Object invokeRest(MethodInfo methodInfo, ServerInfo serverInfo) {
        request = this.client
                .method(methodInfo.getMethod())
                .uri(serverInfo.getUrl() + methodInfo.getUrl(), methodInfo.getParams())
                .accept(MediaType.APPLICATION_JSON);
        ResponseSpec retrieve = null;
        // 判断是否带了body
        if (methodInfo.getBody() != null) {
            // 发出请求
            retrieve = request
                    .body(methodInfo.getBody(), methodInfo.getBodyElementType())
                    .retrieve();
        } else {
            retrieve = request.retrieve();
        }

        retrieve.onStatus(status -> status.value() == 404,
                response -> Mono.just(new RuntimeException("Not Found")));

        Object result = null;
        // 处理body
        if (methodInfo.isReturnFlux()) {
            result = retrieve.bodyToFlux(methodInfo.getReturnElementType());
        } else {
            result = retrieve.bodyToMono(methodInfo.getReturnElementType());
        }
        return result;
    }

}
