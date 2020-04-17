package com.yff.test.test.business.cost;

import com.yff.test.base.BaseTest;
import com.yff.test.page.business.cost.CashFlowItemPage;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * 现金流量项测试
 *
 * @author XXX
 * @version 1.0.0
 * @date 2019/9/6
 */
public class CashFlowItemTest extends BaseTest {
    /**
     * 现金流量项页面类
     */
    private CashFlowItemPage cashFlowItemPage;

    @Test(groups = "jumpToPage", description = "ZX 现金流量项跳转页面", priority = 1)
    @Parameters({"browseNumber", "environment", "language"})
    public void jumpToPageTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 跳转到现金流量项页面 */
        cashFlowItemPage.jumpToPageModule();
    }

    @Test(groups = "newCreateMustInputZero", description = "ZX 现金流量项模块跳转到新建必输字段-0", priority = 2)
    @Parameters({"browseNumber", "environment", "language"})
    public void newCreateMustInputZeroTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 新建必输字段-0 */
        cashFlowItemPage.newCreateMustInputZeroModule();
    }

    @Test(groups = "newCreateMustInputOne", description = "ZX 现金流量项模块跳转到新建必输字段-1", priority = 3)
    @Parameters({"browseNumber", "environment", "language"})
    public void newCreateMustInputOneTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 新建必输字段-1 */
        cashFlowItemPage.newCreateMustInputOneModule();
    }

    @Test(groups = "newCreateMustInputTwo", description = "ZX 现金流量项模块跳转到新建必输字段-2", priority = 4)
    @Parameters({"browseNumber", "environment", "language"})
    public void newCreateMustInputTwoTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 新建必输字段-2 */
        cashFlowItemPage.newCreateMustInputTwoModule();
    }

    @Test(groups = "textBoxFormat", description = "ZX 现金流量项模块新建输入框文本格式校验方法", priority = 5)
    @Parameters({"browseNumber", "environment", "language"})
    public void textBoxFormatTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 新建输入框文本格式校验方法 */
        cashFlowItemPage.textBoxFormatModule();
    }

    @Test(groups = "createCloseByCross", description = "ZX 现金流量项模块新建模块点x方法", priority = 6)
    @Parameters({"browseNumber", "environment", "language"})
    public void createCloseByCrossTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 新建模块点x方法 */
        cashFlowItemPage.createCloseByCrossModule();
    }

    @Test(groups = "createCloseByCancel", description = "ZX 现金流量项模块新建模块点取消方法", priority = 7)
    @Parameters({"browseNumber", "environment", "language"})
    public void createCloseByCancelTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 新建模块点取消方法 */
        cashFlowItemPage.createCloseByCancelModule();
    }

    @Test(groups = "create", description = "ZX 现金流量项模块中新建功能校验", priority = 8)
    @Parameters({"browseNumber", "environment", "language"})
    public void createTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 测试新建功能 */
        cashFlowItemPage.newCreateModule();
    }

    @Test(groups = "createRepeat", description = "ZX 现金流量项模块中新建重复功能校验", priority = 9)
    @Parameters({"browseNumber", "environment", "language"})
    public void createRepeatTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 测试新建功能 */
        cashFlowItemPage.newCreateRepeatModule();
    }

    @Test(groups = "page", description = "ZX 现金流量项模块中页码功能校验", priority = 10)
    @Parameters({"browseNumber", "environment", "language"})
    public void pageTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 测试页码功能 */
        cashFlowItemPage.pageNumModule();
    }

    @Test(groups = "clearBeforeSearch", description = "ZX 现金流量项模块中搜索之前清空模块方法", priority = 11)
    @Parameters({"browseNumber", "environment", "language"})
    public void clearBeforeSearchTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 搜索之前清空模块方法 */
        cashFlowItemPage.clearBeforeSearchModule();
    }

    @Test(groups = "search", description = "ZX 现金流量项模块中搜索功能校验", priority = 12)
    @Parameters({"browseNumber", "environment", "language"})
    public void searchTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 测试搜索功能 */
        cashFlowItemPage.searchModule();
    }

    @Test(groups = "clearAfterSearch", description = "ZX 现金流量项模块中搜索之后清空模块方法", priority = 13)
    @Parameters({"browseNumber", "environment", "language"})
    public void clearAfterSearchTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 搜索之后清空模块方法 */
        cashFlowItemPage.clearModule();
    }

    @Test(groups = "editMustInput", description = "ZX 现金流量项模块中编辑必输字段模块方法", priority = 14)
    @Parameters({"browseNumber", "environment", "language"})
    public void editMustInputTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 编辑必输字段模块方法 */
        cashFlowItemPage.editMustInputModule();
    }

    @Test(groups = "editCloseByCross", description = "ZX 现金流量项模块中编辑点x关闭模块方法", priority = 15)
    @Parameters({"browseNumber", "environment", "language"})
    public void editCloseByCrossTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 编辑点x关闭模块方法 */
        cashFlowItemPage.editCloseByCrossModule();
    }

    @Test(groups = "editCloseByCancel", description = "ZX 现金流量项模块中编辑点取消关闭模块方法", priority = 16)
    @Parameters({"browseNumber", "environment", "language"})
    public void editCloseByCancelTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 编辑点取消关闭模块方法 */
        cashFlowItemPage.editCloseByCancelModule();
    }


    @Test(groups = "edit", description = "ZX 现金流量项模块中编辑功能校验", priority = 17)
    @Parameters({"browseNumber", "environment", "language"})
    public void editTest(@Optional("1") String browseNumber, @Optional("uat") String environment, @Optional("zh_CN") String language) throws Exception {
        /* 初始化页面类 */
        cashFlowItemPage = new CashFlowItemPage(driver, environment, language);

        /* 测试编辑功能 */
        cashFlowItemPage.editModule();
    }
}