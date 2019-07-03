package com.au;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.au.exception.GetGenericServiceFailedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:artificialunintelligent
 * @Date:2019-07-01
 * @Time:14:55
 */
public class DubboCallBackUtil {

    // 当前应用的信息
    private static ApplicationConfig application = new ApplicationConfig();

    // 注册中心信息缓存
    private static Map<String, RegistryConfig> registryConfigCache = new ConcurrentHashMap<>();

    // 各个业务方的ReferenceConfig缓存
    private static Map<String, ReferenceConfig> referenceCache = new ConcurrentHashMap<>();

    static {
        application.setName("consumer-script");
    }

    /**
     *
     * @param interfaceName 接口地址-要求全限定名 例如com.au.service.UserService
     * @param methodName 方法名称
     * @param paramList 参数列表
     * @param address zk注册地址
     * @param version 接口版本号--可不填
     * @param group 接口分组--可不填
     * @return
     * @throws GetGenericServiceFailedException
     */
    public static Object invoke(String interfaceName, String methodName, List<Object> paramList, String address,
                                String version, String group) throws GetGenericServiceFailedException, ClassNotFoundException {
        ReferenceConfig reference = getReferenceConfig(interfaceName, address, group, version);
        if (null != reference) {
            GenericService genericService = (GenericService) reference.get();
            if (genericService == null) {
                throw new GetGenericServiceFailedException();
            }
            Object[] paramObject = null;
            if (!CollectionUtils.isEmpty(paramList)) {
                paramObject = new Object[paramList.size()];
                for (int i = 0; i < paramList.size(); i++) {
                    paramObject[i] = paramList.get(i);
                }
            }
            Object resultParam = genericService.$invoke(methodName, getMethodParamType(interfaceName, methodName), paramObject);
            return resultParam;
        }
        return null;
    }

    /**
     * 获取注册中心信息
     * @param address zk注册地址
     * @param group   dubbo服务所在的组
     * @return
     */
    private static RegistryConfig getRegistryConfig(String address, String group, String version) {
        String key = address + "-" + group + "-" + version;
        RegistryConfig registryConfig = registryConfigCache.get(key);
        if (null == registryConfig) {
            registryConfig = new RegistryConfig();
            if (StringUtils.isNotEmpty(address)) {
                registryConfig.setAddress(address);
            }
            if (StringUtils.isNotEmpty(version)) {
                registryConfig.setVersion(version);
            }
            if (StringUtils.isNotEmpty(group)) {
                registryConfig.setGroup(group);
            }
            registryConfigCache.put(key, registryConfig);
        }
        return registryConfig;
    }

    private static ReferenceConfig getReferenceConfig(String interfaceName, String address,
                                                      String group, String version) throws ClassNotFoundException{
        String referenceKey = interfaceName;
        ReferenceConfig referenceConfig = referenceCache.get(referenceKey);
        if (null == referenceConfig) {
            referenceConfig = new ReferenceConfig<>();
            referenceConfig.setApplication(application);
            referenceConfig.setRegistry(getRegistryConfig(address, group, version));
            Class interfaceClass = Class.forName(interfaceName);
            referenceConfig.setInterface(interfaceClass);
            if (StringUtils.isNotEmpty(version)) {
                referenceConfig.setVersion(version);
            }
            referenceConfig.setGeneric(true);

            referenceCache.put(referenceKey, referenceConfig);
        }
        return referenceConfig;
    }

    private static String[] getMethodParamType(String interfaceName, String methodName) {
        try {
            //创建类
            Class<?> class1 = Class.forName(interfaceName);
            //获取所有的公共的方法
            Method[] methods = class1.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] paramClassList = method.getParameterTypes();
                    String[] paramTypeList = new String[paramClassList.length];
                    int i = 0;
                    for (Class className : paramClassList) {
                        paramTypeList[i] = className.getTypeName();
                        i++;
                    }
                    return paramTypeList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
