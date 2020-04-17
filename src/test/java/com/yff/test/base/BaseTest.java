package com.yff.test.base;

import com.yff.base.DriverBase;
import com.yff.base.JedisBase;
import com.yff.test.page.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import redis.clients.jedis.Jedis;



/**
 * @author YFF
 * @date 2020/4/4
 */
@Slf4j
public class BaseTest {
    public static DriverBase driverBase = new DriverBase();
    public WebDriver driver;
    public WebDriverWait wait;
    public static TimeBaseOpt timePage = new TimeBaseOpt();

    // 需要用到redis则定义
    public static JedisBase jedisBase = new JedisBase();
    public Jedis jedis;


    @BeforeTest(alwaysRun = true)
    @Parameters({"browseNumber", "remoteIP", "browserVersion"})
    public void setUp(@Optional("1") int browseNumber, @Optional() String remoteIP, @Optional("74.0.3729.169") String browserVersion) {
        try {
            /*启动浏览器，并获取指定浏览器的driver放在driverThreadLocalUtil中*/
            driverBase.randomOpenBrowse(browseNumber, remoteIP, browserVersion);
            /*创建JedisPool，并将JedisPool的资源放在threadJedis中，最终存放在jedisThreadLocalUtil中*/
            jedisBase.getJedisPool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClassInit() {
        /*获取driverThreadLocalUtil中的driver对象*/
        driver = driverBase.getDriver();
        /*设置隐式等待及创建driver的WebDriverWait对象*/
        timePage.setTimeouts(driver);
        /*将获取的WebDriverWait对象赋值给wait对象*/
        wait = timePage.getWait();
        /*获取jedisThreadLocalUtil的jedis对象*/
        jedis = jedisBase.getJedis();
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"user", "pwd", "environment", "language"})
    public void loginClass(@Optional("13200000023") String user, @Optional("1qaz2wsx") String pwd, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        LoginPage loginPage = new LoginPage(driver, environment, language);
        /* 登陆系统 */
        /*loginPage.loginRZH_UI(user, pwd);*/
        loginPage.loginRZH_API(user, pwd);
    }

    @AfterTest(alwaysRun = true)
    public void stop() {
        /*关闭driver对象*/
        driverBase.stopDriver();
        /*移除WebDriverWait对象*/
        timePage.releaseWait();
        /*归还jedis对象*/
        jedisBase.returnJedis();
    }
}