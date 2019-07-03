package com.au;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.au.exception.GetGenericServiceFailedException;
import com.au.exception.ParamException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public static Object invoke(String interfaceName, String methodName, List<String> paramTypes, List<Object> paramValues, String address,
                                String version, String group) throws GetGenericServiceFailedException, ParamException {
        ReferenceConfig reference = getReferenceConfig(interfaceName, address, group, version);
        if (null != reference) {
            GenericService genericService = (GenericService) reference.get();
            if (genericService == null) {
                throw new GetGenericServiceFailedException();
            }
            if (Objects.nonNull(paramTypes) && Objects.nonNull(paramValues) && !Objects.equals(paramValues.size(), paramTypes.size())){
                throw new ParamException();
            }
            String[] paramTypeArray = paramTypes.toArray(new String[0]);
            Object[] paramValueArray = paramValues.toArray();
            Object resultParam = genericService.$invoke(methodName, paramTypeArray, paramValueArray);
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
                                                      String group, String version) {
        String referenceKey = interfaceName;
        ReferenceConfig referenceConfig = referenceCache.get(referenceKey);
        if (null == referenceConfig) {
            referenceConfig = new ReferenceConfig<>();
            referenceConfig.setApplication(application);
            referenceConfig.setRegistry(getRegistryConfig(address, group, version));
            referenceConfig.setInterface(interfaceName);
            if (StringUtils.isNotEmpty(version)) {
                referenceConfig.setVersion(version);
            }
            referenceConfig.setGeneric(true);

            referenceCache.put(referenceKey, referenceConfig);
        }
        return referenceConfig;
    }
}
