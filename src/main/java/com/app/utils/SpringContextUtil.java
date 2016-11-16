package com.app.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by yangzhao on 16/8/1.
 */
public class SpringContextUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Object getBean(String contextId){
        return applicationContext.getBean(contextId);
    }
    public <T> T getBean(Class<T> clazz){
         return applicationContext.getBean(clazz);
    }
}
