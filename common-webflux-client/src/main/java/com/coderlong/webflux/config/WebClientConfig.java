package com.coderlong.webflux.config;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static io.netty.channel.ChannelOption.TCP_NODELAY;

import java.time.Duration;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

/**
 * 资源管理
 *
 * @author sailongren
 */
@Configuration
public class WebClientConfig {

    @Bean
    ReactorResourceFactory resourceFactory() {
        ReactorResourceFactory factory = new ReactorResourceFactory();
        // 连接池配置
        // maxConnections
        // 到达maxConnections后， pending线程数量, 其余的直接失败
        ConnectionProvider connectionProvider = ConnectionProvider.builder("tcp-client-pool")
                .maxConnections(1)
                .pendingAcquireTimeout(Duration.ofMillis(60000))
                .pendingAcquireMaxCount(1)
                .build();
        factory.setUseGlobalResources(false);
        factory.setConnectionProvider(connectionProvider);
        factory.setLoopResources(LoopResources.create("MyHttpClient", 30, true));
        return factory;
    }

    @Bean
    WebClient webClient() {
        Function<HttpClient, HttpClient> mapper = client ->
                client.tcpConfiguration(c ->
                        c.option(CONNECT_TIMEOUT_MILLIS, 3000)
                                .option(TCP_NODELAY, true)
                                .doOnConnected(conn -> {
                                    conn.addHandlerLast(new ReadTimeoutHandler(30));
                                    conn.addHandlerLast(new WriteTimeoutHandler(30));
                                }));

        ClientHttpConnector connector =
                new ReactorClientHttpConnector(resourceFactory(), mapper);

        return WebClient.builder().clientConnector(connector).build();
    }

}
