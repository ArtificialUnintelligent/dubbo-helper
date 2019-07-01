package com.au.utils;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.util.Arrays;

/**
 * @author:artificialunintelligent
 * @Date:2019-07-01
 * @Time:10:54
 */
public final class SpringAopUtils {

    private SpringAopUtils() {
    }

    public static Object invoker(Object mockObj, MethodInterceptor interceptor) {
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(mockObj);
        factory.addAdvisor(getAdvisor(mockObj, interceptor));
        return factory.getProxy();
    }

    private static DefaultPointcutAdvisor getAdvisor(Object mockObj, MethodInterceptor interceptor) {
        return new DefaultPointcutAdvisor(getPointcut(mockObj), interceptor);
    }

    private static StaticMethodMatcherPointcut getPointcut(Object mockObj) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        Arrays.stream(mockObj.getClass().getDeclaredMethods()).forEach((method) -> {
            pointcut.addMethodName(method.getName());
        });
        return pointcut;
    }
}
