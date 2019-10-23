# spring-cloud-web
spring cloud web project demo


## 一、配置服务
[官方说明](https://cloud.spring.io/spring-cloud-config/reference/html/)
### 1、使用场景
- 方便不同运行环境切换
- 方便不同机器使用同一套配置，方便扩展服务

## 二、微服务调用
[spring-cloud-openfeign 使用说明](https://github.com/spring-cloud/spring-cloud-openfeign/blob/master/docs/src/main/asciidoc/spring-cloud-openfeign.adoc)  

### 1、名称解释
| 名称 | 解释释义 |
| ---- | ----|
| Ribbon | Netflix开源、基于HTTP和TCP等协议的负载均衡组件。客户端类型的负载均衡。 |
| Hystrix | 提供熔断、隔离、失败回调等功能，能够在程序依赖出现故障时，依然保持可用的组件。 |
| Feign | <p>简化HTTP API调用。直接面向java接口编程，http调用就像本地调用接口一样方便。</p><p>内置Ribbon作为客户端负载均衡。</p>内置Hystrix组件。 |
| openfeign | 基于Feign的开发组件，是spring-cloud种的一个http客户端组件。支持基于SpringMVC注解的Feign接口定义。 |
   
    
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

认证授权服务

推送服务



定时任务
