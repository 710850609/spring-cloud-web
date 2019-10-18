package me.linbo.web.demo.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 1 控制台发布服务器配置参数 curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=web-api.properties&group=DEFAULT_GROUP&content=useLocalCache=服务器配置
 * 2 访问  http://localhost:10000/config/get
 * OUP&content=useLocalCache=true"
 * @author LinBo
 * @date 2019-10-18 17:12
 */
@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigApi {

    @Value("${useLocalCache:本地默认配置}")
    private String useLocalCache;

    @RequestMapping("/get")
    public String get() {
        return useLocalCache;
    }
}
