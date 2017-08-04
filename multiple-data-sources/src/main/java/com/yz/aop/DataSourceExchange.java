package com.yz.aop;

import com.yz.datasources.DataSource;
import com.yz.datasources.DataSourceContextHolder;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by yangzhao on 17/2/7.
 */
@Component
@Aspect
public class DataSourceExchange {

        @AfterReturning("anyMethod()")
        public void afterReturning(){
            DataSourceContextHolder.clearDataSource();
        }

        @AfterThrowing(pointcut = "anyMethod()",throwing = "e")
        public void afterThrowing(Exception e){
            DataSourceContextHolder.clearDataSource();
        }

        @Pointcut("execution(* com.huamai.app.dao..*.*(..)) || execution(* com.huamai.patient.dao..*.*(..)) || execution(* com.huamai.patient.huayan.dao..*.*(..))||execution(* com.huamai.patient.familyrecord.dao..*.*(..))" +
                " || execution(* com.hbt.consultation_service.dao..*.*(..))|| execution(* com.hbt.patient_diet_service.dao..*.*(..))|| execution(* com.hbt.goods_service.dao..*.*(..))")
        private void anyMethod() {}

        @Before("anyMethod()")
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
