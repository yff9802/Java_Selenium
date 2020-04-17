package com.yff.test.page.business.cost;


import com.yff.test.common.zxPageCommon;
import com.yff.test.data.business.cost.CashFlowItemData;
import com.yff.test.locator.business.cost.CashFlowItemLocator;
import com.yff.util.ClockTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.yff.test.base.BaseTest.jedisBase;

/**
 * 现金流量项页面
 *
 * @author YFF
 * @version 2.0.0
 * @date 2020/03/10
 */
@Slf4j
public class CashFlowItemPage extends zxPageCommon {
    /**
     * 构造器
     */
    public CashFlowItemPage(WebDriver driver,String environment, String language){
        // 调用父类构造器
        super(driver, environment,language);
    }

    /* =============================现金流量项页面-1 ============================== */
    /**
     * 通过搜索框进入到现金流量项这个页签
     * @throws Exception
     */
    public void jumpToPageModule() throws Exception{
        log.info("跳转进入现金流量项页面");
        // 跳转到指定页面
        jumpByClick(CashFlowItemData.FUNCTION_NAME);
        Assert.assertEquals("现金流量项",getFirstTabName());
    }

    /* ============================== 新建必输字段-0校验方法-2 ============================== */
    /**
     * 空值点保存
     * @throws Exception
     */
    public void newCreateMustInputZeroModule() throws Exception {
        log.info("新建必输字-全不输段校验");
        // 点击新建按钮
        clickButton(700, CashFlowItemLocator.CREATE_BTN, 800);
        // 新建流量项代码
        String currentTime = ClockTool.getCurrentTime();
        String[] data = {"" , "", "", ""};
        inputAllData("slide", data);
        clickButton(500, CashFlowItemLocator.SAVE_BTN, 500);
        int Flag = driver.findElements(CashFlowItemLocator.RED_PLEASE_ENTER).size();
        Assert.assertTrue(Flag == 2);
        clickButton(500, CashFlowItemLocator.CANCEL_BTN, 800);
    }


    /* ============================== 新建必输字段-1校验方法-3 ============================== */
    /**
     * 现金流量项名称空点取消
     * @throws Exception
     */
    public void newCreateMustInputOneModule() throws Exception {
        log.info("新建必输字段第一个不输入校验");
        // 点击新建按钮
        clickButton(700, CashFlowItemLocator.CREATE_BTN, 800);
        // 新建流量项代码
        String currentTime = ClockTool.getCurrentTime();
        String[] data = {"" , "", currentTime, ""};
        inputAllData("slide", data);
        clickButton(500, CashFlowItemLocator.SAVE_BTN, 2000);
        Assert.assertTrue(isElementContained(CashFlowItemLocator.RED_PLEASE_ENTER));
        clickButton(500, CashFlowItemLocator.CANCEL_BTN, 800);
    }

    /* ============================== 新建必输字段-2校验方法-4 ============================== */
    /**
     * 现金流量项代码空值点取消
     * @throws Exception
     */
    public void newCreateMustInputTwoModule() throws Exception {
        log.info("新建必输字段第二个不输入校验");
        // 点击新建按钮
        clickButton(700, CashFlowItemLocator.CREATE_BTN, 800);
        // 新建现金流量项代码
        String currentTime = ClockTool.getCurrentTime();
        String[] data = {"" , "", "", currentTime};
        inputAllData("slide", data);
        clickButton(500, CashFlowItemLocator.SAVE_BTN, 2000);
        Assert.assertTrue(isElementContained(CashFlowItemLocator.RED_PLEASE_ENTER));
        clickButton(500, CashFlowItemLocator.CANCEL_BTN, 800);
    }

    /* ============================== 新建输入框文本格式校验方法-5 ============================== */
    /**
     * 输入特殊字符(!@#$%^&*()743146159)现金流量项名称
     * 预期情况：特殊字符不能输入
     * @throws Exception
     */
    public void textBoxFormatModule() throws Exception {
        log.info("新建输入框文本格式校验");
        // 点击新建按钮
        clickButton(700, CashFlowItemLocator.CREATE_BTN, 800);
        // 特殊字符
        String Special = SpecialCharacters();
        String[] data = {"" , "", Special, "ui测试"};
        inputAllData("slide", data);
        // 获取输入的文本框
        Thread.sleep(700);
        String get_text = driver.findElement(CashFlowItemLocator.CASH_FLOW_CODE).getAttribute("value");
//        Thread.sleep(5000000);
        System.out.println(get_text);
        Assert.assertTrue(isNumeric(get_text));
        clickButton(500, CashFlowItemLocator.CANCEL_BTN, 800);
    }

    /* ============================== 新建模块点x方法-6 ============================== */
    /**
     * 新建模块点开之后点击右上角的x关闭
     * @throws Exception
     */
    public void createCloseByCrossModule() throws Exception {
        log.info("新建点x关闭");
        Thread.sleep(10000);
        int Flag = closeSlideByCross(CashFlowItemLocator.CREATE_BTN);
        assert Flag == 0;
    }
    /* ============================== 新建模块点取消方法-7 ============================== */
    /**
     * 新建模块点开之后点击取消按钮关闭
     * @throws Exception
     */
    public void createCloseByCancelModule() throws Exception {
        log.info("新建点取消关闭");
        int Flag = closeSlideByCancel(CashFlowItemLocator.CREATE_BTN);
        assert Flag == 0;
    }

    /* ============================== 新建模块方法-8 ============================== */
    /**
     * 新建功能
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void newCreateModule() throws Exception {
        log.info("新建功能测试");
        // 点击账套
        clickButton(600, By.xpath("//div[@id='setOfBookId']"), 1000);
        if (jedisBase.getKey("accountSetCode") != null && jedisBase.getKey("accountSetName") != null) {
            clickButton(600, By.xpath("//ul[@role='listbox']/li[text()='" + jedisBase.getKey("accountSetCode") + " - " + jedisBase.getKey("accountSetName") + "']"), 0);
        }
        // 若 redis 中没有值，默认选择框选择第一行
        else {
            clickButton(600, By.xpath("//ul[@role='listbox']/li[1]"), 0);
        }
        // 点击新建按钮
        clickButton(CashFlowItemLocator.CREATE_BTN);
        // 获取当前时间并保存在 jedis 中
        String currentTime = ClockTool.getCurrentTime();
        String[] data = {CashFlowItemData.ENABLE, "", currentTime, currentTime};
        inputAllData("slide", data);
        // 点击保存
        clickButton(CashFlowItemLocator.SAVE_BTN);
        assert isToastCorrect("success");
        jedisBase.setKey("cashFlowItemCode", currentTime);
        jedisBase.setKey("cashFlowItemName", currentTime);
    }

    /* ============================== 新建重复模块方法-9 ============================== */
    /**
     * 新建重复
     * @throws Exception
     */
    public void newCreateRepeatModule() throws Exception {
        // 新建重复
        clickButton(600,By.xpath("//div[@id='setOfBookId']"),0);
        if(jedisBase.getKey("accountSetCode") != null && jedisBase.getKey("accountSetName") != null) {
            clickButton(600,By.xpath("//ul[@role='listbox']/li[text()='" + jedisBase.getKey("accountSetCode") + " - " + jedisBase.getKey("accountSetName") + "']"),0);
        }
        // 若 redis 中没有值，默认选择框选择第一行
        else{
            clickButton(600,By.xpath("//ul[@role='listbox']/li[1]"),0);
        }
        clickButton(CashFlowItemLocator.CREATE_BTN);
        // 从jedis 中获取当前时间
        String[] data02 = {CashFlowItemData.ENABLE, "", jedisBase.getKey("cashFlowItemCode"), jedisBase.getKey("cashFlowItemName")};
        inputAllData("slide", data02);
        // 点击保存
        clickButton(CashFlowItemLocator.SAVE_BTN);
        //校验是否有失败的toast
        assert isToastCorrect("error");
        // 点击取消
        clickButton(CashFlowItemLocator.CANCEL_BTN);
    }


    /* ============================== 页码模块方法-10 ============================== */
    /**
     * 页码测试
     * @throws Exception
     */
    public void pageNumModule() throws Exception {
        log.info("页码功能测试");
        // 滑动到最底端
        scrollToBottom();
        Assert.assertTrue(PAGE_TEST("tab", CashFlowItemData.FIVE_PERPAGE, CashFlowItemData.TWO));
    }

    /* ============================== 搜索之前清空模块方法-11 ============================== */
    public void clearBeforeSearchModule() throws Exception {
        log.info("搜索之前清空");
        boolean Flag = CLEAR_BEFORE_SEARCH("tab", 1);
        Assert.assertTrue(Flag);
    }

    /* ============================== 搜索模块方法-12 ============================== */
    /**
     * 搜索功能
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void searchModule() throws Exception {
        log.info("搜索功能测试");
        // 若 redis 中有值，选择匹配到的 li
        clickButton(600,By.xpath("//div[@id='setOfBookId']"),1000);
        if(jedisBase.getKey("accountSetCode") != null && jedisBase.getKey("accountSetName") != null) {
            clickButton(600,By.xpath("//ul[@role='listbox']/li[text()='" + jedisBase.getKey("accountSetCode") + " - " + jedisBase.getKey("accountSetName") + "']"),0);
        }
        // 若 redis 中没有值，默认选择框选择第一行
        else{
            clickButton(600,By.xpath("//ul[@role='listbox']/li[1]"),0);
        }
        String[] data={"",jedisBase.getKey("cashFlowItemCode"),jedisBase.getKey("cashFlowItemName")};
        inputAllData("tab", data);
        clickButton(600,CashFlowItemLocator.SEARCH_BUTTON,700);
        int[] td={1,2};
        //搜索之后的数据条数
        Assert.assertTrue(isDataMatchTable("tab",data,td));
    }

    /* ============================== 清空模块方法-13 ============================== */
    /**
     * 清空功能
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void clearModule() throws Exception {
        log.info("清空功能测试");
        // 获取下方页码文本
        WebElement PAGE_TEST = driver.findElement(CashFlowItemLocator.PAGE_FONT);
        String ELEMENT_TEST=PAGE_TEST.getText();
        clickButton(600,By.xpath("//div[@id='setOfBookId']"),1000);
        if(jedisBase.getKey("accountSetCode") != null && jedisBase.getKey("accountSetName") != null) {
            clickButton(600,By.xpath("//ul[@role='listbox']/li[text()='" + jedisBase.getKey("accountSetCode") + " - " + jedisBase.getKey("accountSetName") + "']"),0);
        }
        // 若 redis 中没有值，默认选择框选择第一行
        else{
            clickButton(600,By.xpath("//ul[@role='listbox']/li[1]"),0);
        }
        String[] data={"",jedisBase.getKey("cashFlowItemCode"),jedisBase.getKey("cashFlowItemName")};
        inputAllData("tab", data);
        clickButton(0,CashFlowItemLocator.CLEAR_BUTTON,600);
        // 再次获取下方页码文本
        WebElement PAGE_TEXT = driver.findElement(CashFlowItemLocator.PAGE_FONT);
        String ELEMENT_TEXT=PAGE_TEXT.getText();
        Assert.assertTrue(ELEMENT_TEST.equals(ELEMENT_TEXT));
        //判断搜索之后的数据条数是否发生改变
    }

    /* ============================== 编辑必输字段模块方法-14 ============================== */
    public void editMustInputModule() throws Exception {
        log.info("编辑必输字段模块方法");
        // 点击编辑按钮
        clickButton(600,CashFlowItemLocator.EDIT_SPAN,600);
        // 获取存储的字符串长度
        String Flag = jedisBase.getKey("cashFlowItemCode");
        int length = Flag.length();
        // 清除输入框里面的数据
        for (int i = 1;i<=length;i++){
            driver.findElement(CashFlowItemLocator.CASH_NAME).sendKeys(Keys.BACK_SPACE);
        }
        clickButton(800,CashFlowItemLocator.SAVE_BTN,600);
        // 校验空值提示
        Assert.assertTrue(isElementContained(CashFlowItemLocator.RED_PLEASE_ENTER));
        // 点击取消
        clickButton(CashFlowItemLocator.CANCEL_BTN);
    }

    /* ============================== 编辑点x关闭模块方法-15 ============================== */
    public void editCloseByCrossModule() throws Exception {
        log.info("编辑点x关闭");
        Thread.sleep(10000);
        int Flag = closeSlideByCross(CashFlowItemLocator.EDIT_SPAN);
        assert Flag == 0;
    }

    /* ============================== 编辑点取消关闭模块方法-16 ============================== */
    public void editCloseByCancelModule() throws Exception {
        log.info("编辑点取消关闭");
        int Flag = closeSlideByCancel(CashFlowItemLocator.EDIT_SPAN);
        assert Flag == 0;
    }

    /* ============================== 编辑模块方法-17 ============================== */
    /**
     * 编辑功能
     *
     * @throws Exception 输入数据时候找不到匹配的弹出框抛出的异常
     */
    public void editModule() throws Exception {
        log.info("编辑功能测试");
        // 点击编辑按钮
        clickButton(600,CashFlowItemLocator.EDIT_SPAN,600);
        // 获取当前时间并保存到 jedis 中
        String currentTime = ClockTool.getCurrentTime();
        String[] data = {CashFlowItemData.ENABLE, "", "", currentTime };
        inputAllData("slide", data);
        clickButton(600,CashFlowItemLocator.SAVE_BTN,0);
        //校验是否有成功的toast
        assert isToastCorrect("success");
        jedisBase.setKey("cashFlowItemName", currentTime);
    }
}
