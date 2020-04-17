package com.yff.test.page.business.cost;

import com.sun.media.jfxmedia.logging.Logger;
import com.yff.test.common.YffPageCommon;
import com.yff.test.data.business.cost.CashAffairClassData;
import com.yff.test.locator.business.cost.CashAffairClassLocator;
import com.yff.util.ClockTool;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.Arrays;
import static com.yff.test.base.BaseTest.jedisBase;

/**
 * 现金事务分类页面
 *
 * @author YFF
 * @version 1.0.0
 * @date 2019/09/05
 **/
@Slf4j
public class CashAffairClassPage extends YffPageCommon {
    /*变量popPageNum  存储弹出框中页面数据条数*/
    private static Integer popPageNum;

    /*变量pageNum 存储二级界面数据条数*/
    private static Integer pageNum;

    /*变量twoUrl 用来存储二级页面的URL*/
    private static String twoUrl = "";

    /*变量 用来存储现金事务类型*/
    private static String typeName = "";

    /*新建的数据 由于存储在jedisBase是在判断添加成功之后，所以提出来*/
    private static String cashAffairCode;
    private static String cashAffairName;

    /**
     * 构造器
     */
    public CashAffairClassPage(WebDriver driver, String environment, String language) {
        // 调用父类构造器
        super(driver, environment, language);
    }

    /* =============================现金事务分类页面 ============================== */
    /**
     * 跳转到现金事务分类页面
     *
     * @throws Exception xpath找不到的异常
     */
    public void jumpToPageModule() throws Exception {
        log.info("跳转进入现金事务分类页面");
        // 跳转到指定页面
        jumpByClick(CashAffairClassData.FUNCTION_NAME);
        Assert.assertEquals("现金事务分类",getFirstTabName());
    }

    /**
     * 一级页面页码功能
     * @throws Exception 其他异常
     */
    public void pageNumModule() throws Exception {
        log.info("页码功能测试");
        // 滑动到最底端
        scrollToBottom();
        // 页码选择
        choosePageNum("tab", CashAffairClassData.TEN_PERPAGE, CashAffairClassData.TWO);
    }

    /**
     * 新建取消
     * @throws Exception 其他异常
     */
    public void createCancelModule()throws Exception{
        log.info("新建取消");
        /*等待几秒*/
        Thread.sleep(2000);
        /*点击新建按钮*/
        clickButton(100, CashAffairClassLocator.CREATE_BUTTON, 100);
        Assert.assertEquals("新建现金事务分类",getFirstTabName());
        /*点击取消按钮*/
        clickButton(100, CashAffairClassLocator.CANCEL_BUTTON, 1000);
        Assert.assertEquals("现金事务分类",getFirstTabName());
    }

    /**
     * 新建空值校验
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void createNullCheckModule() throws Exception {
        log.info("新建空值校验");
        /*等待几秒*/
        Thread.sleep(2000);
        /*点击新建按钮*/
        clickButton(100, CashAffairClassLocator.CREATE_BUTTON, 100);
        /*点击保存按钮*/
        clickButton(100, CashAffairClassLocator.SAVE_BUTTON, 100);
        /*校验是否有空值的判断*/
        Assert.assertTrue(isPromptContained("tab"));
        /*点击取消*/
        clickButton(100, CashAffairClassLocator.CANCEL_BUTTON, 100);
        /*校验是否返回到一级界面*/
        Assert.assertEquals("现金事务分类",getFirstTabName());
    }

    /**
     * 正常新建
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void newCreateModule() throws Exception {
        /**
         * 正常新建
         * */
        /*进入页面*/
        jumpToPageModule();
        /*等待几秒*/
        Thread.sleep(2000);
        /*新建操作*/
        CreateModule();
        /*判断是否提示成功*/
        Assert.assertTrue(isQuickToastCorrect("success","保存成功"));
        /*存储相关数据*/
        jedisBase.setKey("cashAffairCode", cashAffairCode);
        jedisBase.setKey("cashAffairName", cashAffairName);
        /*点击返回图标*/
        clickButton(100, CashAffairClassLocator.BACK_I, 2000);
        Assert.assertEquals("现金事务分类",getFirstTabName());
    }

    /**
     * 重复新建
     *
     * @throws Exception 其他异常
     */
    public void repeatCreateModule() throws Exception {
        log.info("重复新建");
        /*等待几秒*/
        Thread.sleep(2000);
        CreateModule();
        /*校验是否错误*/
        Assert.assertTrue(isQuickToastCorrect("error","现金事务分类代码不允许重复"));
        /*点击取消按钮*/
        clickButton(100, CashAffairClassLocator.CANCEL_BUTTON, 100);
        Assert.assertEquals("现金事务分类",getFirstTabName());
    }

    /**
     * 单独搜索
     *
     * @throws Exception 其他异常
     */
    public void aloneSearchModule() throws Exception {
        log.info("单独搜索");
        clickMoreIcon();
        String data[]=new String[]{"","付款"};
        String data1[]=new String[]{"","",jedisBase.getKey("cashAffairCode")};
        String data2[]=new String[]{"","","",jedisBase.getKey("cashAffairName")};
        String[] checkData;
        int[] td_index;
        /**
         * 1.现金事务类型
         * */
        inputAllDataByData("tab",data);
        clickButton(100, CashAffairClassLocator.SEARCH_BUTTON, 1000);
        /*获取子数组 ，数组下标从1到4（不含4） 即为不包括账套的数据*/
        checkData = Arrays.copyOfRange(data, 1, 2);
        /*定义判断数据对应的列数*/
        td_index=new int[]{1};
        Assert.assertTrue(isDataMatchTable("tab", checkData, td_index));
        clickButton(100,CashAffairClassLocator.CANCEL_BUTTON,100);

        /**
         * 2.分类代码
         * */
        inputAllDataByData("tab",data1);
        clickButton(100, CashAffairClassLocator.SEARCH_BUTTON, 1000);
        /*获取子数组 ，数组下标从1到4（不含4） 即为不包括账套的数据*/
        checkData = Arrays.copyOfRange(data1, 2, 3);
        /*定义判断数据对应的列数*/
        td_index=new int[]{2};
        Assert.assertTrue(isDataMatchTable("tab", checkData, td_index));
        clickButton(100,CashAffairClassLocator.CANCEL_BUTTON,100);

        /**
         * 3.分类名称
         * */
        inputAllDataByData("tab",data2);
        clickButton(100, CashAffairClassLocator.SEARCH_BUTTON, 1000);
        /*获取子数组 ，数组下标从1到4（不含4） 即为不包括账套的数据*/
        checkData = Arrays.copyOfRange(data2, 3, 4);
        /*定义判断数据对应的列数*/
        td_index=new int[]{3};
        Assert.assertTrue(isDataMatchTable("tab", checkData, td_index));
        clickButton(100,CashAffairClassLocator.CANCEL_BUTTON,100);
    }

    /**
     * 联合搜索
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void searchModule() throws Exception {
        log.info("联合搜索");
        clickMoreIcon();
        String[] data;
        // 若 redis 中有值，选择匹配到的 li
        if (jedisBase.getKey("accountSetCode") != null && jedisBase.getKey("accountSetName") != null) {
            data = new String[]{jedisBase.getKey("accountSetCode") + " - " + jedisBase.getKey("accountSetName"), typeName, jedisBase.getKey("cashAffairCode"), jedisBase.getKey("cashAffairName")};
        }
        // 若 redis 中没有值，默认选择框选择第一行
        else {
            data = new String[]{"", typeName, jedisBase.getKey("cashAffairCode"), jedisBase.getKey("cashAffairName")};
        }
        /*填写数据*/
        inputAllData("tab", data);
        /*点击搜索按钮*/
        clickButton(100, CashAffairClassLocator.SEARCH_BUTTON, 1000);
        /*获取子数组 ，数组下标从1到4（不含4） 即为不包括账套的数据*/
        String[] checkData = Arrays.copyOfRange(data, 1, 4);
        /*定义判断数据对应的列数*/
        int[] td_index = {1, 2, 3};
        Assert.assertTrue(isDataMatchTable("tab", checkData, td_index));
    }


    /**
     * 清空功能
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void clearModule() throws Exception {
        log.info("清空功能测试");
        /*首先获取页面数据条数*/
        Integer num1 = getPageNumber(CashAffairClassLocator.PAGENUM_LI);
        /*点击清空按钮*/
        clickButton(100, CashAffairClassLocator.CLEAR_BUTTON, 100);
        /*判断是否清空*/
        Assert.assertTrue(isCleared("tab","UI_001 - UI_001"));
        /*判断是否数据显示正常*/
        Assert.assertTrue(num1.equals(getPageNumber(CashAffairClassLocator.PAGENUM_LI)));
    }

    /**
     * 现金事务分类<==>现金事务分类详情
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void editModule1() throws Exception {
        log.info("现金事务分类<==>现金事务分类详情");
        /*点击搜索之后的第一行*/
        clickButton(100, CashAffairClassLocator.EDIT_TR, 100);
        Assert.assertEquals("现金事务分类详情",getFirstTabName());
        /*点击返回*/
        clickButton(100,CashAffairClassLocator.BACK_I,100);
        Assert.assertEquals("现金事务分类",getFirstTabName());
    }

    /**
     * 编辑并取消
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void editModule2() throws Exception {
        log.info("编辑并取消");
        searchModule();
        /*点击搜索之后的第一行*/
        clickButton(100, CashAffairClassLocator.EDIT_TR, 100);
        // 点击编辑超链接
        clickButton(CashAffairClassLocator.EDIT_A);
        /*点击取消按钮*/
        clickButton(100,By.xpath("//form//button[@class='ant-btn']"),100);
        Assert.assertFalse(isElementContained(By.xpath("//form//button[@class='ant-btn']")));
        Assert.assertTrue(isElementContained(CashAffairClassLocator.EDIT_A));
    }

    /**
     * 编辑并确定
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void editModule3() throws Exception {
        log.info("编辑并确定");
        // 点击编辑超链接
        clickButton(CashAffairClassLocator.EDIT_A);
        // 获取当前时间
        String cashAffairName = ClockTool.getCurrentTime();
        String[] data = {"", "", "", cashAffairName, CashAffairClassData.ENABLE};
        /*输入值*/
        inputAllData("tab", data);
        /*点击保存*/
        clickButton(100, CashAffairClassLocator.EDIT_CREATE_BUTTON, 100);
        /*判断是否提示成功*/
        Assert.assertTrue(isQuickToastCorrect("success","操作成功"));
        /*判断值是否修改成功*/
        Assert.assertTrue(cashAffairName.equals(driver.findElement(By.xpath("(//div[@class='basic-info']//div[@class='ant-card-body']//div[@class='ant-col ant-col-8'])[last()-1]/div[2]")).getText()));
        /*编辑成功之后就存储在Redis中*/
        jedisBase.setKey("cashAffairName", cashAffairName);

        /*获取二级界面的数据条数*/
        if (isElementContained(CashAffairClassLocator.PAGETOTALTEXT_LI)) {
            pageNum = getPageNumber(CashAffairClassLocator.PAGETOTALTEXT_LI);
        } else {
            pageNum = 0;
        }
    }

    /**
     * 打开并关闭现金流量项LOV框
     * @throws Exception 其他异常
     */
    public void popModule1()throws Exception{
        log.info("打开并关闭现金流量项LOV框");
        /*点击添加按钮*/
        clickButton(100, CashAffairClassLocator.ADD_BUTTON, 100);
        Assert.assertTrue(isElementContained("//div[@class='ant-modal-title' and text()='现金流量项']"));
        /*点击取消*/
        clickButton(100,By.xpath("(//div[@class='ant-modal-footer']//button[@class='ant-btn'])[last()]"),1000);
        Thread.sleep(2000);
        Assert.assertTrue(isElementContained("//div[contains(@class,'ant-modal-mask-hidden')]"));
    }

    /**
     * LOV框页码
     * @throws Exception 其他异常
     */
    public void popModule2()throws Exception{
        log.info("LOV框页码");
        /*点击添加按钮*/
        clickButton(100, CashAffairClassLocator.ADD_BUTTON, 100);
        Assert.assertTrue(isElementContained("//div[@class='ant-modal-title' and text()='现金流量项']"));
        // 页码选择
        choosePageNum("modal", CashAffairClassData.TEN_PERPAGE, CashAffairClassData.TWO);
        choosePageNum("modal", CashAffairClassData.TEN_PERPAGE, "1");
        //获取弹出框中数据条数并赋值给变量popPageNum 后面的弹出框清空和搜索有用到
        popPageNum = getPageNumber(CashAffairClassLocator.POP_PAGETOTALTEXT_LI);
    }

    /**
     * LOV框单独搜索
     * @throws Exception 其他异常
     */
    public void popModule3()throws Exception{
        log.info("LOV框单独搜索");
        String data[]=new String[]{"UI001","UI001"};
        int td[]=new int[]{2,3};
        Assert.assertTrue(isAloneSearch("modal",data,td,new String[]{"0","0"}));
    }

    /**
     * LOV框联合搜索
     * @throws Exception 其他异常
     */
    public void popModule4()throws Exception{
        log.info("LOV框联合搜索");
        String data[]=new String[]{"UI001","UI001"};
        inputAllDataByData("modal",data);
        clickButton(100,By.xpath("(//form//button[@class='ant-btn ant-btn-primary'])[last()]"),100);
        int td[]=new int[]{2,3};
        Assert.assertTrue(isDataMatchTable("modal",data,td));
    }

    /**
     * LOV框清空
     * @throws Exception 其他异常
     */
    public void popModule5()throws Exception{
        log.info("LOV框清空");
        int num = getPageNumber(CashAffairClassLocator.POP_PAGETOTALTEXT_LI);
        clickButton(100, CashAffairClassLocator.POP_CLEAR_BUTTON, 100);
        /*校验是否清空*/
        Assert.assertTrue(isCleared("modal"));
        Assert.assertTrue(num==getPageNumber(CashAffairClassLocator.POP_PAGETOTALTEXT_LI));
    }

    /**
     * LOV框禁用数据
     * @throws Exception 其他异常
     */
    public void popModule6()throws Exception{
        log.info("LOV框禁用数据");
        String[] data = new String[]{"UI22267", "UI_YFF_现金事务分类测试用_勿删_勿改状态"};
        /*填写数据*/
        inputAllDataByData("modal", data);
        /*点击搜索*/
        clickButton(100, CashAffairClassLocator.POP_SEARCH_BUTTON, 100);
        /*校验是否没有数据*/
        Assert.assertTrue(!isElementContained(CashAffairClassLocator.PAGETOTALTEXT_LI));
    }

    /**
     * 勾选数据并确定
     * @throws Exception 其他异常
     */
    public void popModule7()throws Exception{
        log.info("勾选数据并确定");
        clickButton(100,By.xpath("(//form//button[@class='ant-btn'])[last()]"),100);
        String data[]=new String[]{"",""};
        inputAllDataByData("modal",data);
        clickButton(100,By.xpath("(//form//button[@class='ant-btn ant-btn-primary'])[last()]"),100);

        /*点击勾选框*/
        clickButton(100, CashAffairClassLocator.POP_SELECT_INPUT, 100);
        /*点击确定按钮*/
        clickButton(100, By.xpath("(//div[@class='ant-modal-footer']//button[@class='ant-btn ant-btn-primary'])[last()]"), 100);
        Assert.assertTrue(isQuickToastCorrect("success","操作成功"));
        Thread.sleep(2000);
        /*刷新页面*/
        driver.navigate().refresh();
        Thread.sleep(6000);
        closeDashBoardTab();

        if (popPageNum >= 10) {
            Assert.assertTrue(getPageNumber(CashAffairClassLocator.PAGENUM_LI) == pageNum + 10);
            pageNum = getPageNumber(CashAffairClassLocator.PAGENUM_LI);
        } else {
            Assert.assertTrue(getPageNumber(CashAffairClassLocator.PAGENUM_LI) == pageNum + popPageNum);
            pageNum = getPageNumber(CashAffairClassLocator.PAGENUM_LI);
        }
    }


    /**
     * 现金事务分类详情页面的页码功能检测
     *
     * @throws Exception 其他异常
     */
    public void detailPageNumModule() throws Exception {
        //测试页码
        scrollToBottom();
        // 页码选择
        choosePageNum("tab", CashAffairClassData.TEN_PERPAGE, CashAffairClassData.TWO);

    }

    /**
     * 添加现金流量项之后，勾选现金流量项中启用禁用状态
     *
     * @throws Exception 其他异常
     */
    public void selectStatusModule() throws Exception {
        log.info("勾选启用功能测试");

        /*首先获取勾选框label的class属性*/
        String text1 = driver.findElement(CashAffairClassLocator.SELECT_STATUS_LAVEL).getAttribute("class");

        /*点击默认的input勾选框*/
        clickButton(100, CashAffairClassLocator.SELECT_STATUS_LAVEL, 100);

        /*再次获取勾选框label的calss属性*/
        String text2 = driver.findElement(CashAffairClassLocator.SELECT_STATUS_LAVEL).getAttribute("class");

        /*校验两次的属性不一样*/
        Assert.assertFalse(text1.equals(text2));
    }

    /**
     * 添加现金流量项之后，勾选现金流量项中默认的勾选框
     * 校验点击默认属性变化及只能默认一个
     *
     * @throws Exception 其他异常
     */
    public void selectDefaultModule() throws Exception {
        log.info("勾选默认功能测试");

        /**
         * 校验点击之后属性改变
         * */
        /*首先获取勾选框label的class属性*/
        String text1 = driver.findElement(CashAffairClassLocator.SELECT_DEFAULT_LABEL).getAttribute("class");

        /*点击默认的input勾选框*/
        clickButton(CashAffairClassLocator.SELECT_DEFAULT_LABEL);

        /*再次获取勾选框label的calss属性*/
        String text2 = driver.findElement(CashAffairClassLocator.SELECT_DEFAULT_LABEL).getAttribute("class");

        /*校验两次的属性不一样*/
        Assert.assertFalse(text1.equals(text2));

        /**
         * 校验点击另一个的默认之后，前面的默认是否消失
         * */
        /*点击默认的input勾选框*/
        clickButton(CashAffairClassLocator.SELECT_DEFAULT_LABEL2);
        /*校验第一行的默认数据是否改变*/
        Assert.assertFalse(text2.equals(driver.findElement(CashAffairClassLocator.SELECT_DEFAULT_LABEL).getAttribute("class")));
    }

    /**
     * 校验禁用和非付款类型的现金事务分类是否不能搜索到
     *
     * @throws Exception 其他异常
     */
    public void BillModule() throws Exception {
        /*关闭当前页面*/
        closeFirstTab();
        System.out.println(language);
        jumpByUrl(CashAffairClassData.BILL_URL[num1][num2]);

        Thread.sleep(5000);
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath("//i[@class='anticon anticon-right ant-collapse-arrow']"))).perform();
        Thread.sleep(5000);
        /*点击添加付款信息按钮*/
        clickButton(100, By.xpath("//span[text()='添加付款信息']/.."), 100);
        /*点击付款用途的搜索图标*/
        clickButton(100, By.xpath("(//i[@class='anticon anticon-search ant-input-search-icon'])[last()]"), 100);

        /**
         * 判断预付款的类型的字段是否不能搜索到
         * */
        /*填写数据*/
        String[] data1 = new String[]{"22267", ""};
        inputAllDataByData("modal", data1);
        /*点击搜索按钮*/
        clickButton(100, By.xpath("//div[@class='ant-modal-body']//button[@class='ant-btn ant-btn-primary']"), 100);
        /*断言是否没有数据*/
        Assert.assertFalse(isElementContained("//div[@class='ant-modal-body']//tbody//tr[1]"));

        /**
         * 判断禁用的是否不能搜索到
         * */
        /*点击清空按钮*/
        clickButton(100, By.xpath("//div[@class='ant-modal-body']//button[@class='ant-btn']"), 100);
        /*输入数据*/
        data1 = new String[]{"YF002", ""};
        inputAllData("modal", data1);
        /*点击搜索按钮*/
        clickButton(100, By.xpath("//div[@class='ant-modal-body']//button[@class='ant-btn ant-btn-primary']"), 100);
        /*断言是否没有数据*/
        Assert.assertFalse(isElementContained("//div[@class='ant-modal-body']//tbody//tr[1]"));

        /**
         * 判断正常的付款类型的数据是否能搜索到
         * */
        /*点击清空按钮*/
        clickButton(100, By.xpath("//div[@class='ant-modal-body']//button[@class='ant-btn']"), 100);
        /*输入数据*/
        data1 = new String[]{"01", ""};
        inputAllData("modal", data1);
        /*点击搜索按钮*/
        clickButton(100, By.xpath("//div[@class='ant-modal-body']//button[@class='ant-btn ant-btn-primary']"), 100);
        /*断言是否没有数据*/
        Assert.assertTrue(isElementContained("//div[@class='ant-modal-body']//tbody//tr[1]"));

    }


    /**
     * 新建过程中的操作
     * 提供给新建及重复新建的方法使用
     *
     * @throws Exception 新建及新建重复共用方法中出现的错误
     */
    private void CreateModule() throws Exception {
        /*点击更多图标*/
        clickMoreIcon();
        String[] dataSetOfBook;

        if (jedisBase.getKey("accountSetCode") != null && jedisBase.getKey("accountSetName") != null) {
            dataSetOfBook = new String[]{jedisBase.getKey("accountSetCode") + " - " + jedisBase.getKey("accountSetName")};
        } else {
            dataSetOfBook = new String[]{""};
        }
        inputAllDataByData("tab", dataSetOfBook);

        // 点击左侧新建按钮
        clickButton(100, CashAffairClassLocator.CREATE_BUTTON, 100);
        /*根据值是否为null判断是否为初次新建或者重复新建*/
        if (cashAffairCode == null && cashAffairName == null) {
            cashAffairCode = cashAffairName = ClockTool.getCurrentTime();
        }
        String[] data = {"", "付款", cashAffairCode, cashAffairName, CashAffairClassData.ENABLE};
        /*插入数据*/
        inputAllDataByData("tab", data);
        /*点击保存按钮*/
        clickButton(100, CashAffairClassLocator.SAVE_BUTTON, 100);
    }


}
