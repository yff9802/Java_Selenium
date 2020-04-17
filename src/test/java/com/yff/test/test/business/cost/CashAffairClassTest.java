package com.yff.test.test.business.cost;

import com.yff.test.base.BaseTest;
import com.yff.test.page.business.cost.CashAffairClassPage;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * 现金事务分类页测试
 *
 * @author YFF
 * @version 1.0.0
 * @date 2019/09/05
 **/
public class CashAffairClassTest extends BaseTest {
    /**
     * 现金事务分类页面类
     */
    private CashAffairClassPage cashAffairClassPage;

    @Test(priority = 1, description = "YFF  现金事务分类 进入页面")
    @Parameters({"environment", "language"})
    public void test1(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.jumpToPageModule();
    }

    @Test(priority = 2, description = "YFF  现金事务分类 页码")
    @Parameters({"environment", "language"})
    public void test2(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.pageNumModule();
    }

    @Test(priority = 3, description = "YFF  现金事务分类 新建取消")
    @Parameters({"environment", "language"})
    public void test3(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.createCancelModule();
    }

    @Test(priority = 4, description = "YFF  现金事务分类 新建空值校验")
    @Parameters({"environment", "language"})
    public void test4(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.createNullCheckModule();
    }

    @Test(priority = 5, description = "YFF  现金事务分类 正常新建")
    @Parameters({"environment", "language"})
    public void test5(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.newCreateModule();
    }

    @Test(priority = 6, description = "YFF  现金事务分类 新建重复")
    @Parameters({"environment", "language"})
    public void test6(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.repeatCreateModule();
    }

    @Test(priority = 7, description = "YFF  现金事务分类 单独搜索")
    @Parameters({"environment", "language"})
    public void test7(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.aloneSearchModule();
    }

    @Test(priority = 8, description = "YFF  现金事务分类 联合搜索")
    @Parameters({"environment", "language"})
    public void test8(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.searchModule();
    }

    @Test(priority = 9, description = "YFF  现金事务分类 清空")
    @Parameters({"environment", "language"})
    public void test9(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.clearModule();
    }

    @Test(priority = 10, description = "YFF  现金事务分类 现金事务分类<==>现金事务分类详情")
    @Parameters({"environment", "language"})
    public void test10(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.editModule1();
    }

    @Test(priority = 11, description = "YFF  现金事务分类 编辑并取消")
    @Parameters({"environment", "language"})
    public void test11(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.editModule2();
    }

    @Test(priority = 12, description = "YFF  现金事务分类 编辑并确定")
    @Parameters({"environment", "language"})
    public void test12(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.editModule3();
    }

    @Test(priority = 13, description = "YFF  现金事务分类 打开并关闭现金流量项LOV框")
    @Parameters({"environment", "language"})
    public void test13(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.popModule1();
    }

    @Test(priority = 14, description = "YFF  现金事务分类 LOV框页码")
    @Parameters({"environment", "language"})
    public void test14(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.popModule2();
    }

    @Test(priority = 15, description = "YFF  现金事务分类 LOV框单独搜索")
    @Parameters({"environment", "language"})
    public void test15(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.popModule3();
    }

    @Test(priority = 16, description = "YFF  现金事务分类 LOV框联合搜索")
    @Parameters({"environment", "language"})
    public void test16(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.popModule4();
    }

    @Test(priority = 17, description = "YFF  现金事务分类 LOV框清空")
    @Parameters({"environment", "language"})
    public void test17(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.popModule5();
    }

    @Test(priority = 18, description = "YFF  现金事务分类 LOV框禁用数据")
    @Parameters({"environment", "language"})
    public void test18(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.popModule6();
    }

    @Test(priority = 19, description = "YFF  现金事务分类 勾选数据并确定")
    @Parameters({"environment", "language"})
    public void test19(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.popModule7();
    }

    @Test(priority = 20, description = "YFF  现金事务分类 现金事务分类详情页面的页码功能检测")
    @Parameters({"environment", "language"})
    public void test20(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.detailPageNumModule();
    }

    @Test(priority = 21, description = "YFF  现金事务分类 添加现金流量项之后，勾选现金流量项中启用禁用状态")
    @Parameters({"environment", "language"})
    public void test21(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.selectStatusModule();
    }

    @Test(priority = 22, description = "YFF  现金事务分类 添加现金流量项之后，勾选现金流量项中默认的勾选框")
    @Parameters({"environment", "language"})
    public void test22(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.selectDefaultModule();
    }

    @Test(priority = 23, description = "YFF  现金事务分类 校验禁用和非付款类型的现金事务分类是否不能搜索到")
    @Parameters({"environment", "language"})
    public void test23(@Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashAffairClassPage = new CashAffairClassPage(driver, environment, language);
        cashAffairClassPage.BillModule();
    }


}
