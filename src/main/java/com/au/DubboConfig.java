package com.au;

/**
 * @author:artificialunintelligent
 * @Date:2019-06-26
 * @Time:10:59
 */
public class DubboConfig {

    private String applicationName = "";
    private String registryAddress;
    /** 默认10000 */
    private int referenceTimeOut = 10000;
    private String monitorProtocol = "registry";

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public int getReferenceTimeOut() {
        return referenceTimeOut;
    }

    public void setReferenceTimeOut(int referenceTimeOut) {
        this.referenceTimeOut = referenceTimeOut;
    }

    public String getMonitorProtocol() {
        return monitorProtocol;
    }

    public void setMonitorProtocol(String monitorProtocol) {
        this.monitorProtocol = monitorProtocol;
    }
}
