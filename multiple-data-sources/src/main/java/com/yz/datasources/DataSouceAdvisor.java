package com.huamai.datasources;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 增强类
 *
 * @author yangzhao
 *         create by 17/10/20
 */
@Component("dataSourceAdvisor")
public class DataSouceAdvisor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        Object aThis = methodInvocation.getThis();
        //设置默认数据库
        DataSourceContextHolder.setDataSourceName("defaultSource");

        DataSource dataSource = aThis.getClass().getAnnotation(DataSource.class);
        if (dataSource!=null){
            DataSourceContextHolder.setDataSourceName(dataSource.value());
        }
        dataSource = method.getAnnotation(DataSource.class);
        if (dataSource!=null){
            DataSourceContextHolder.setDataSourceName(dataSource.value());
        }
        Object proceed = methodInvocation.proceed();
        DataSourceContextHolder.clearDataSource();
        return proceed;
    }
}
