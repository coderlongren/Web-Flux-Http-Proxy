# Web-Flux-Http-Proxy
1. 使用WebClient 提供WebFlux风格的 Http接口代理
2. 支持自定义http线程池
3. fluent的API

### How to Use
使用 `@ApiServerScan(packages = "com.coderlong.webflux.service")`扫描固定package下service接口。  

`service`接口使用 `@ApiServer(value = "http://localhost:8081", microName = "webflux-server")`标识服务名  

声明`HTTP Path`

```java

@GetMapping(ROOT + "/")
Flux<User> getAllUser();

@GetMapping(ROOT + "/{id}")
Mono<User> getUserById(@PathVariable("id") String id);

@DeleteMapping(ROOT + "/{id}")
Mono<Void> deleteUserById(@PathVariable("id") String id);

```

1. `Simple-Http-provider` 启动 `8081`端口
 
2. `Simple-Http-client` 启动`8888`端口， 请求 `8888` 把http请求自动转化为reactor操作符

### FIX ME
> 如果想实现http服务动态，可以使用Nacos/ZK provider启动时注册IP和端口，client启动时扫描@ApiServer.microName 把IP动态注入到value
> 实现服务发现的功能.
>
> 负载均衡可以在代理层 加一层SLB
