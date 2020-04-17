package com.yff.test.base;

import com.yff.utils.ThreadLocalUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * WebDriverWait类的相关设置（driver的隐式等待相关设置）
 * 参考链接：
 *          https://www.cnblogs.com/xiaozhaoboke/p/11130416.html
 *          隐式等待只能用于元素的等待
 *          一般来说，是在父类中设置好隐式等待，全局设置driver的等待时间
 *          然后针对具体的场景，使用wait.until来进行显示等待
 *
 *          http://www.imooc.com/wenda/detail/593246
 *          隐式等待和显示等待混合使用会出现异常问题，所以推荐以下步骤来使用
 *          测试开始时设置隐式等待->取消隐式等待（即设置为0）->
 *          使用wait.until显示等待->重置隐式等待（即将隐式等待设置为最初的值)
 *
 *          https://blog.csdn.net/sinat_41774836/article/details/88965281
 *          https://blog.csdn.net/u014104286/article/details/53858435
 *
 * @author YFF
 * @date 2020/4/4
 */
public class TimeBaseOpt extends BaseTest{
    /**
     * 创建WebDriverWait的ThreadLocal和ThreadLocalUtil对象
     * */
    private static ThreadLocalUtil<WebDriverWait> waitThreadLocalUtil = new ThreadLocalUtil<>();
    private static ThreadLocal<WebDriverWait> threadWait = new ThreadLocal<>();

    /**
     *设置driver对象的WebDriverWait对象，并设置最大超时时间
     * */
    public void setWait(WebDriver webDriver){
        waitThreadLocalUtil.setThreadValue( threadWait,  new WebDriverWait(webDriver,200));
    }

    /**
     *获取WebDriverWait对象
     * */
    public WebDriverWait getWait(){
        return threadWait.get();
    }

    /**
     * 移除threadWait中WebDriverWait对象
     * */
    public void releaseWait(){
        threadWait.remove();
    }

    /**
     * 设置driver的超时时间
     * */
    public void setTimeouts(WebDriver webDriver) {
        if(webDriver != null){
            /*1.调用方法创建driver的WebDriverWait对象*/
            setWait(webDriver );
            /*2.最大化窗口*/
            webDriver.manage().window().maximize();
            /*3.设置隐式等待 时间为10s*/
            webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            /*4.设置页面加载超时时间 时间为20s*/
            webDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
            /*3.设置等待异步脚本的超时时间 时间为10s*/
            webDriver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS );
        }
    }
}
