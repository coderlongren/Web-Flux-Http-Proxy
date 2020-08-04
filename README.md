# Web-Flux-Http-Proxy
1. 使用WebClient 提供WebFlux风格的 Http接口代理
2. 支持自定义http线程池
3. fluent的API

### How to Use
1. common-wenflux-client主要包含必要注解， 接口代理功能。

2. 实例模块

`Simple-Http-provider` 启动 `8081`端口
 
`Simple-Http-client` 启动`8888`端口， 请求 `8888` 非阻塞访问`provider`工程接口

