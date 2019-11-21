package me.linbo.web.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author LinBo
 * @date 2019-11-12 11:05
 */
@Component
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T> T getBean(Class<T> clazz) {
        Objects.requireNonNull(context, "未注入Spring Context");
        return context.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, String beanName) {
        Objects.requireNonNull(context, "为注入Spring Context");
        if (beanName == null || beanName.isEmpty()) {
            return getBean(clazz);
        }
        return context.getBean(beanName, clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
