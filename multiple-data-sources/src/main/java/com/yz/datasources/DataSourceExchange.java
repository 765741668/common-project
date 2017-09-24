package com.yz.datasources;

import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by yangzhao on 17/2/7.
 */
public class DataSourceExchange {

        public void afterReturning(){
            DataSourceContextHolder.clearDataSource();
        }

        public void afterThrowing(Exception e){
            DataSourceContextHolder.clearDataSource();
        }

        private void anyMethod() {}

        public void before(JoinPoint joinPoint){
            Object target = joinPoint.getTarget();

            DataSource dataSource = target.getClass().getAnnotation(DataSource.class);
            if (dataSource!=null){
                DataSourceContextHolder.setDataSourceName(dataSource.value());
            }

            String name = joinPoint.getSignature().getName();
            Method[] declaredMethods = target.getClass().getDeclaredMethods();
            Method[] declaredMethods1 = target.getClass().getSuperclass().getDeclaredMethods();
            Method[] methods = (Method[]) ArrayUtils.addAll(declaredMethods, declaredMethods1);
            Optional<Method> methodOptional = Arrays.stream(methods).filter((method) -> {
                if (method.getName().equals(name)) {
                    return true;
                }
                return false;
            }).findFirst();
            Method method = methodOptional.get();
            if (method.getAnnotation(DataSource.class)!=null)
            {
                DataSource datasource = method.getAnnotation(DataSource.class);
                DataSourceContextHolder.setDataSourceName(datasource.value());
            }

        }
}
