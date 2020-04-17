package com.yff.test.locator.business.cost;

import org.openqa.selenium.By;

/**
 * 现金流量项页面定位
 *
 * @author YFF
 * @version 1.0.0
 * @date 2019/9/5
 */
public class CashFlowItemLocator {
    /* ============================== 新建重复区域 xpath 定位 ============================== */
    /**
     * 红色请输入提示
     */
    public static final By RED_PLEASE_ENTER = By.xpath("//div[contains(@class,'slide-content')]//div[@class='ant-form-explain']");

    /**
     * 现金流量项代码
     */
    public static final By CASH_FLOW_CODE = By.xpath("(//span[@class='ant-form-item-children'])[6]//input");

    /* ============================== 搜索区域 xpath 定位 ============================== */
    /**
     * 搜索按钮
     */
    public static final By SEARCH_BUTTON = By.xpath("//form[contains(@class,'search-area')]//button[1]");

    /**
     * 清空按钮
     */
    public static final By CLEAR_BUTTON = By.xpath("//form[contains(@class,'search-area')]//button[2]");

    /* ============================== 新建区域 xpath 定位 ============================== */
    /**
     * 新建的按钮
     */
    public static final By CREATE_BTN = By.xpath("//div[@class='table-header']//button[1]");

    /**
     * 保存按钮
     */
    public static final By SAVE_BTN = By.xpath("//span[text()='保 存']/..");

    /**
     * 取消按钮
     */
    public static final By CANCEL_BTN = By.xpath("//span[text()='取 消']/..");

    /**
     * 下方页码数据
     */
    public static final By PAGE_FONT = By.xpath("//ul[@class='ant-pagination ant-table-pagination mini']/li[1]");

    /* ============================== 编辑区域 xpath 定位 ============================== */
    /**
     * 编辑链接
     */
    public static final By EDIT_SPAN = By.xpath("(//tbody[@class='ant-table-tbody'])[2]/tr[1]");

    /**
     * 编辑现金流量项名称输入框
     */
    public static final By CASH_NAME = By.xpath("(//div[contains(@class,'slide-content')]//input)[2]");

}
