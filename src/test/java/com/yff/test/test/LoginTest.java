package com.yff.test.test;

import com.yff.test.base.BaseTest;
import org.testng.annotations.Test;

/**
 * 登陆页测试
 *
 * @author abcnull
 * @version 1.0.0
 * @date 2019/8/26
 */
public class LoginTest extends BaseTest {
    @Test(groups = "other", description = "登陆模块中登陆功能校验")
    public void loginFunction(){
        // 已经通过 @BeforeClass 注解实现了登陆操作
        // 请见 D:\JavaProject\rongzhihui-webui\src\test\java\com\test\pc\base\BaseTest.java
    }
}
