# dubbo-helper
一个dubbo调用的小型脚本

## 泛化调用
调用DubboCallBackUtil.invoke方法，入参信息如下：
* interfaceName(String) 接口地址-要求全限定名 例如com.au.service.UserService
* methodName(String) 方法名称
* paramList(List<Object>) 参数列表
* address(String) zk地址
* version(String) 接口版本号--可不填
* group(String) 接口分组--可不填<br>

返回Object，直接强制转换成返回结果类使用
需要捕获GetGenericServiceFailedException异常

## 引入目标接口调用
步骤如下：
1. 填写DubboConfig信息，需要指定应用名称和zk地址
2. 调用DubboServiceUtil.buildDubboServiceUtil.getService并传入填写DubboConfig信息获取DubboServiceUtil类
3. 调用DubboServiceUtil.getService并传入被调用接口类即可获取到被调用接口实例
4. 通过接口实例调用具体方法
