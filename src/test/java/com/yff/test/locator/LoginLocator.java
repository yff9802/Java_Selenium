package com.yff.test.locator;

import org.openqa.selenium.By;

/**
 * 登陆页定位
 *
 * @author YFF
 * @version 1.0.0
 * @date 2019/8/27
 */
public class LoginLocator {
    /**
     * 用户名的 input
     */
    public static final By USER_INPUT = By.xpath("//div[@class='account-class-wrap']//input[1]");

    /**
     * 密码的 input
     */
    public static final By PWD_INPUT = By.xpath("//div[@class='account-class-wrap']//input[2]");

    /**
     * 登陆按钮
     */
    public static final By LOGIN_BTN = By.xpath("//button[@class='ant-btn ant-btn-primary ant-btn-lg']");

    /**
     * 登陆之后页面的仪表盘字段的定位
     */
    public static final By AFTERLOGIN_DIV = By.xpath("//div[@class='ant-tabs-tab-unclosable']");
}
