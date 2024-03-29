# spring-cloud-web
spring cloud web project demo



## 一、配置服务
[官方说明](https://cloud.spring.io/spring-cloud-config/reference/html/)
### 1、使用场景
- 方便不同运行环境切换
- 方便不同机器使用同一套配置，方便扩展服务

项目选用阿里开源的 [Nacos](https://nacos.io/zh-cn/)，可从[GitHub](https://github.com/alibaba/nacos/releases)下载最新版本并运行。

## 二、微服务调用
[spring-cloud-openfeign 使用说明](https://github.com/spring-cloud/spring-cloud-openfeign/blob/master/docs/src/main/asciidoc/spring-cloud-openfeign.adoc)  

### 1、名称解释
| 名称 | 解释释义 |
| ---- | ----|
| Ribbon | Netflix开源、基于HTTP和TCP等协议的负载均衡组件。客户端类型的负载均衡。 |
| Hystrix | 提供熔断、隔离、失败回调等功能，能够在程序依赖出现故障时，依然保持可用的组件。 |
| Feign | <p>简化HTTP API调用。直接面向java接口编程，http调用就像本地调用接口一样方便。</p><p>内置Ribbon作为客户端负载均衡。</p>内置Hystrix组件。 |
| openfeign | 基于Feign的开发组件，是spring-cloud种的一个http客户端组件。支持基于SpringMVC注解的Feign接口定义。 |
   
### 2、注意事项
```io.github.openfeign:feign-core``` 在```10.3.0```版本之前，不支持传入```@SpringQueryMap```修饰类种父类属性[BUG](https://github.com/OpenFeign/feign/pull/960)
    
## 三、网关
[spring-cloud-gateway 说明](https://cloud.spring.io/spring-cloud-gateway/reference/html/)
    
### 1、路由
[路由](https://cloud.spring.io/spring-cloud-gateway/reference/html/#gateway-request-predicates-factories)

### 2、限流
[Redis RateLimiter](https://cloud.spring.io/spring-cloud-gateway/reference/html/#redis-ratelimiter)

### 3、熔断
[Hystrix Filter](https://cloud.spring.io/spring-cloud-gateway/reference/html/#hystrix)
 
### 4、跨越请求
[CORS配置](https://cloud.spring.io/spring-cloud-gateway/reference/html/#cors-configuration)
 
### 5、  


### 四、认证授权
#### 1、名词解释
- 资源服务
- 认证服务


https://www.cnblogs.com/bigben0123/p/9207825.html
https://www.jianshu.com/p/4089c9cc2dfd
  
网关
    鉴权
    路由权重
    熔断
    黑白名单
    路由转发

监控服务
    数据库
    服务提供者
    服务消费者

用户服务



推送服务



定时任务
