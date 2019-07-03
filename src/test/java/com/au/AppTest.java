package com.au;

import com.au.exception.GetGenericServiceFailedException;
import com.au.exception.InitializationFailedException;
import com.jiaxuan.heaven.book.dubbo.api.IFieldDubboService;
import com.jiaxuan.heaven.book.dubbo.domain.base.FieldBase;
import com.jiaxuan.watch.dogs.api.UserService;
import com.jiaxuan.watch.dogs.api.domain.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testService(){
        DubboConfig dubboConfig = new DubboConfig();
        dubboConfig.setApplicationName("");
        dubboConfig.setRegistryAddress("");
        DubboServiceUtil dubboServiceUtil = null;
        try {
            dubboServiceUtil = DubboServiceUtil.buildDubboServiceUtil(dubboConfig);
        } catch (InitializationFailedException e) {
            e.printStackTrace();
        }
        assert dubboServiceUtil != null;
        //这里引入需要测试的接口类，例如UserService
//        UserService userService = dubboServiceUtil.getService(UserService.class);
//        User user = userService.get("");
//        Assert.assertNotNull(user);
    }

    @Test
    public void testCallBack(){
        List<Object> params = new ArrayList<>();
        String id = "";
        params.add(id);
        try {
            //以下参数替换为自己项目的信息
            Object r = DubboCallBackUtil.invoke("", "", params, "", null, null);
            Assert.assertNotNull(r);
        } catch (GetGenericServiceFailedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
