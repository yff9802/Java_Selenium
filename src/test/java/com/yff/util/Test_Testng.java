package com.yff.util;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * @author YFF
 * @version 1.0.0
 * @date 2020/04/15
 **/
@Slf4j
public class Test_Testng {

    @Test
    public void test1(){
      log.info("test1");
    }

    @Test
    public void test2(){
        log.info("test2");
    }

    @AfterMethod
    public void test3(ITestResult iTestResult){
        log.info("\n"+iTestResult.getInstance().getClass().getPackage().getName());
        /*输入包名 com.yff.util*/
        log.info("\n"+iTestResult.getInstanceName());
        /*输出至类名 com.yff.util.Test_Testng */
    }
}
