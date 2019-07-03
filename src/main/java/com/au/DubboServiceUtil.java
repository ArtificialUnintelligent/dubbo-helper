package com.au;

import java.lang.reflect.Method;

import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.fastjson.JSON;
import com.au.exception.InitializationFailedException;
import com.au.utils.SpringAopUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

/**
 * @author:artificialunintelligent
 * @Date:2019-06-26
 * @Time:10:56
 */
public class DubboServiceUtil {

    private static DubboServiceUtil dubboServiceUtil;

    private DubboConfig dubboConfig;
    private ApplicationConfig applicationConfig;
    private RegistryConfig registryConfig;
    private MonitorConfig monitorConfig;

    public static DubboServiceUtil buildDubboServiceUtil(DubboConfig dubboConfig) throws InitializationFailedException {
        if (dubboServiceUtil == null){
            synchronized (DubboServiceUtil.class){
                if (dubboServiceUtil == null){
                    dubboServiceUtil = new DubboServiceUtil(dubboConfig);
                }
            }
        }
        return dubboServiceUtil;
    }

    private DubboServiceUtil(DubboConfig dubboConfig) throws InitializationFailedException {
        this.dubboConfig = dubboConfig;
        if (dubboConfig == null || StringUtils.isBlank(dubboConfig.getApplicationName()) ||
                StringUtils.isBlank(dubboConfig.getRegistryAddress())) {
            throw new InitializationFailedException();
        }
        applicationConfig = new ApplicationConfig();
        applicationConfig.setName(dubboConfig.getApplicationName());

        registryConfig = new RegistryConfig();
        registryConfig.setAddress(dubboConfig.getRegistryAddress());

        monitorConfig = new MonitorConfig();
        monitorConfig.setProtocol(dubboConfig.getMonitorProtocol());
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        LogMethodInterceptor interceptor = new LogMethodInterceptor(clazz);
        return (T) SpringAopUtils.invoker(cache.get(getReferenceConfig(clazz)), interceptor);
    }

    @SuppressWarnings("unchecked")
    private ReferenceConfig getReferenceConfig(Class<?> clazz) {
        ReferenceConfig referenceConfig = new ReferenceConfig();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setMonitor(monitorConfig);
        referenceConfig.setTimeout(dubboConfig.getReferenceTimeOut());
        referenceConfig.setInterface(clazz);
        return referenceConfig;
    }

    private class LogMethodInterceptor implements MethodInterceptor {

        private Class<?> clazz;

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            Method method = methodInvocation.getMethod();
            System.out.println("====Dubbo====>服务：" + clazz.getSimpleName() + "." + method.getName());
            System.out.println("====Dubbo====>参数：" + JSON.toJSONString(methodInvocation.getArguments()));
            Object result = methodInvocation.proceed();
            System.out.println("====Dubbo====>结果：" + JSON.toJSONString(result));
            return result;
        }

        public LogMethodInterceptor(Class<?> clazz) {
            this.clazz = clazz;
        }
    }
}
