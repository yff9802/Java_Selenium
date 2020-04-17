package com.yff.test.locator.business.cost;

import org.openqa.selenium.By;

/**
 * 现金事务分类页面定位
 * @author  YFF
 * @version 1.0.0
 * @date  2019/09/05
 **/
public class CashAffairClassLocator {

    /* ============================== 首页中 xpath 定位 ============================== */
    /**
     * 搜索按钮
     */
    public static final By SEARCH_BUTTON = By.xpath("//form//button[@class='ant-btn ant-btn-primary']");

    /**
     * 清空按钮
     */
    public static final By CLEAR_BUTTON = By.xpath("//form//button[@class='ant-btn']");

    /**
     * 新建按钮
     */
    public static final By CREATE_BUTTON = By.xpath("//div[@class='table-header']//button[@class='ant-btn ant-btn-primary']");

    /**
     * 页面中显示数据条数 li
     */
    public static final By PAGENUM_LI=By.xpath("((//div[@class='out-content'])[last()]//ul[contains(@class,'ant-pagination ant-table-pagination')]/li[@class='ant-pagination-total-text'])[last()]");

    /**
     * 搜索之后显示的第一行数据，即为点击进入现金事务分类详情页面的xptah
     */
    public static  final By EDIT_TR= By.xpath("(//tbody[@class='ant-table-tbody']/tr[1])[last()]");


    /* ============================== 新建页面的xpath 定位 ============================== */
    /**
     * 保存按钮
     */
    public static final By SAVE_BUTTON = By.xpath("//form//button[@type='submit']");

    /**
     * 取消按钮
     * */
    public static final By CANCEL_BUTTON=By.xpath("//form//button[@class='ant-btn']");


    /*===============================现金事务分类详情页面 ===============================*/
    /**
     *返回图标
     */
    public static final By BACK_I=By.xpath("(//div[@class='out-content']//i[@class='anticon anticon-arrow-left'])[last()]");

    /**
     * 编辑链接
     */
    public static final By EDIT_A = By.xpath("//div[@class='ant-card-extra']/a[1]");

    /**
     * 现金事务分类详情页面中点击编辑之后保存按钮
     */
    public static  final By EDIT_CREATE_BUTTON=By.xpath("//form//button[@class='ant-btn ant-btn-primary']");

    /**
     * 现金事务分类详情页面 数据条数Xpath
     * */
    public static final By PAGETOTALTEXT_LI=By.xpath("//div[@id='payment_transaction_detail']//ul[contains(@class,'ant-pagination ant-table-pagination')]/li[@class='ant-pagination-total-text']");


    /**
     * 添加按钮
     */
    public static final By ADD_BUTTON=By.xpath("//div[@class='table-header']//button[@class='ant-btn ant-btn-primary']");

    /**
     * 弹出框的页码条
     */
    public static final By POP_PAGE_UL=By.xpath("//div[@class='ant-modal-body']//ul[contains(@class,'ant-pagination ant-table-pagination')]");

    /**
     * 弹出框中数据条数的xpath
     */
    public static final By POP_PAGETOTALTEXT_LI=By.xpath("//div[@class='ant-modal-body']//ul[contains(@class,'ant-pagination ant-table-pagination')]/li[@class='ant-pagination-total-text']");


    /**
     * 弹出框搜索按钮
     * */
    public static final By POP_SEARCH_BUTTON=By.xpath("//div[@class='ant-modal-content']//form//button[@class='ant-btn ant-btn-primary']");

    /**
     * 弹出框清空按钮
     * */
    public static final By POP_CLEAR_BUTTON=By.xpath("//div[@class='ant-modal-content']//form//button[@class='ant-btn']");

    /**
     * 弹出框取消按钮
     * */
    public static final By POP_CANCEL_BUTTON=By.xpath("//div[@class='ant-modal-footer']//button[@class='ant-btn']");

    /**
     * 弹出框确定按钮
     * */
    public static final By POP_DEFAULT_BUTTON=By.xpath("//div[@class='ant-modal-footer']//button[@class='ant-btn ant-btn-primary']");

    /**
     * 弹出框中勾选全部的input框
     * */
    public static final By POP_SELECT_INPUT=By.xpath("//div[@class='ant-modal-content']//thead[@class='ant-table-thead']/tr[1]/th[1]//label");

    /**
     * 现金事务分类详情页面默认勾选框的label
     */
    public static final By SELECT_DEFAULT_LABEL=By.xpath("//tbody/tr[1]/td[3]//label");

    /**
     * 现金事务分类详情页面 第二行数据的 默认勾选框的label
     * */
    public static final By SELECT_DEFAULT_LABEL2=By.xpath("//tbody/tr[2]/td[3]//label");


    /**
     *现金事务分类详情页面中启用勾选框的label
     */
    public static  final  By SELECT_STATUS_LAVEL=By.xpath("//tbody/tr[1]/td[4]//label");
}
