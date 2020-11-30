package com.elias.common.holder;

import com.elias.common.exception.SpringContextNotInitializedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author chengrui
 * <p>create at: 2020/11/27 3:43 下午</p>
 * <p>description: </p>
 */
@Slf4j
@Component
public class SpringContextHolder<T> implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("start to set org.springframework.context.ApplicationContext for class SpringContextHolder......");
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            String errMsg = "org.springframework.context.ApplicationContext have not set for class SpringContextHolder";
            log.error(errMsg);
            throw new SpringContextNotInitializedException(errMsg);
        }
        return applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }
}
