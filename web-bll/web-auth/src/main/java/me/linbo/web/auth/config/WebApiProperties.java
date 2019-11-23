package me.linbo.web.auth.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;
import java.util.Map;

/**
 * @author LinBo
 * @date 2019-11-2 11:58
 */
@Data
@ConditionalOnProperty(prefix = "web-api")
public class WebApiProperties {

    private List<Map<String, WebClientProperties>> clients;

}

@Data
class WebClientProperties {

    private String secret;
}