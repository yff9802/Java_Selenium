package com.yff.test.common;

import com.yff.util.ClockTool;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * @author YFF
 * @date 2020/4/4
 */
@Slf4j
public class YffPageCommon extends PageCommon {
    /**
     * 构造器
     *
     * @param driver 子类传入的驱动
     */
    public YffPageCommon(WebDriver driver, String environment, String language) {
        super(driver, environment, language);
    }


    /**
     * 搜索栏目处点击更多图标的方法
     *
     * @throws Exception xpath定位找不到的异常
     */
    protected void clickMoreIcon() throws Exception {
        if (isElementContained("(//a[@class='toggle-button']/i[@class='anticon anticon-down'])[last()]")) {
            clickButton(1500, By.xpath("(//a[@class='toggle-button']/i[@class='anticon anticon-down'])[last()]"), 1500);
        }
    }

    /**
     * 点击Modal框中的更多按钮
     *
     * @throws Exception xpath定位找不到的异常
     */
    protected void clickModalMoreIcon() throws Exception {
        /*点击模态框中的更多图标*/
        if(isElementContained("(//div[@class='ant-modal-body']//i[@class='anticon anticon-down'])[last()]")){
            clickButton(100,By.xpath("(//div[@class='ant-modal-body']//i[@class='anticon anticon-down'])[last()]"),100);
        }
    }



    /**
     * 点击返回按钮，并校验是否返回到指定的页面的方法
     * 若返回页面正确，返回true   若返回页面不正确，返回false
     *
     * @param url 指定返回的页面，一般某个功能的一级界面
     * @param by  返回按钮的对象
     * @return 返回true或false true为返回页面符合预期，false为返回页面不符合预期
     * @throws Exception xpath找不到，导致的点击不到异常
     */
    protected boolean isbackCheck(String url, By by) throws Exception {
        String currentUrl = driver.getCurrentUrl();
        clickButton(by);
        Long startTime = System.currentTimeMillis();
        /*在3秒内，如果页面发生变化，则跳出循环，获取点击按钮之后的页面超链接*/
        while ((System.currentTimeMillis() - startTime) / 1000 < 3) {
            if (!currentUrl.equals(driver.getCurrentUrl())) {
                break;
            }
        }
        if (url.equals(driver.getCurrentUrl())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 审批流监控的操作
     *
     * @param formTypeName 单据类型，由于单据类型不同，审批流监控中搜索的数据不一样
     * @param command      命令是通过还是驳回  "通过"  "驳回"
     * @param documentCode 单据编号
     * @param status       单据状态
     * @throws Exception 其他异常
     */
    protected void approvalprocess(String formTypeName, String command, String documentCode, String status) throws Exception {
        log.info("在审批流监控页面中审批单据");
        /*进入审批流监控页面*/
        jumpByClick("审批流监控");
        /*点击更多*/
        if (isElementContained("//a[@class='toggle-button']/i[@class='anticon anticon-down']")) {
            clickButton(1500, By.xpath("//a[@class='toggle-button']/i[@class='anticon anticon-down']"), 1500);
        }
        String[] data;
        // 若 redis 中有值，选择匹配到的 li
        data = new String[]{formTypeName,"" ,documentCode, "", status};
        inputAllDataByData("tab", data);
        /*点击搜索*/
        clickButton(100, By.xpath("//form[@class='ant-form ant-form-inline ant-advanced-search-form search-area']//button[@class='ant-btn ant-btn-primary']"), 100);

        if ("通过".equals(command)) {
            /*新页面点击通过*/
            clickButtonNoWait(100, By.xpath("//tbody/tr[1]/td[3]/div"), 100);
            /*点击确定*/
            clickButton(100, By.xpath("//div[@class='ant-modal-footer']//button[@class='ant-btn ant-btn-primary']"), 100);
            /*刷新页面*/
            driver.navigate().refresh();
        } else if ("驳回".equals(command)) {
            /*新页面点击通过*/
            clickButtonNoWait(100, By.xpath("//tbody/tr[1]/td[4]/div"), 100);
            /*点击确定*/
            clickButton(100, By.xpath("//div[@class='ant-modal-footer']//button[@class='ant-btn ant-btn-primary']"), 100);
            /*刷新页面*/
            driver.navigate().refresh();
        } else {
            log.info("参数不符合规范");
            throw new Exception("参数不符合规范");
        }

    }


    /**
     * 传当前框类型，被检测数据数组，需要匹配的列数组，返回是否匹配成功的布尔值
     * 与PageCommon中不同的点在于：最后定位文本中有一点不同 目前应用于 费用预提单、项目申请单
     * 费用预提单文本：td/sapn[last()]/span
     * 项目申请单文本：td/span[last()]
     *
     * @param boxType tab，modal 或者 slide
     * @param data    需要和 table 列表中第几列作比较的 String 数组数据
     * @param td      需要和 data[] 数组中数据作比较的列数 int 数组。PS：data["001", "张三", "男"]，td[1, 3, 6]，即表示检测"001"
     *                是否在第 1 列中被检测到，并且"张三"是否在第 3 列中被检测到，并且"男"是否在第 6 列被检测到，返回布尔类型
     * @return 返回 data[] 数据是否在 td[] 这些列中被检测到的布尔值
     * @throws Exception 你输入的弹框类型有误或者暂不支持
     */
    protected boolean pirIsDataMatchTable(String boxType, String[] data, int[] td) throws Exception {
        boolean flag = true;
        // 外框定位
        String parentDiv = "";
        // 当前 table 中是第几行
        String tableRowBox = "";
        // 当前 table 中是第几列
        String tableColumnBox = "";
        // 如果匹配到 tab 标签页
        if (boxType.equals("tab")) {
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
        }
        // 如果匹配到 modal 模态框
        else if (boxType.equals("modal")) {
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-content']";
        }
        // 如果匹配到 slide 滑动框
        else if (boxType.equals("slide")) {
            log.info("现在匹配到滑动框");
            parentDiv = "//div[contains(@class,'slide-frame animated slideInRight')]";
        }
        // 或者什么都没匹配到
        else {
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }
        // 滑动使得列表和顶端对齐
        scrollElementTopToTop(By.xpath(parentDiv + "//div[@class='ant-table-wrapper']//table/tbody"));
        // 循环列表中所有的行
        for (int i = 1; i <= rowsOfTable(boxType); i++) {
            // 若 flag 不真直接跳出
            if (!flag) {
                break;
            }
            // 当前在哪一行
            tableRowBox = parentDiv + "//div[@class='ant-table-wrapper']//table/tbody/tr[" + i + "]";
            // 循环 td[] 中指定的列
            for (int j = 0; j < td.length; j++) {
                // 若 flag 不真直接跳出
                if (!flag) {
                    break;
                }

                // 当前在哪一列
                /*if(isElementContained(tableRowBox + "/td[" + td[j] + "]/span[last()]/span")){
                    tableColumnBox=tableRowBox + "/td[" + td[j] + "]/span[last()]/span";
                }else if(isElementContained(tableRowBox + "/td[" + td[j] + "]/span[last()]")){
                    tableColumnBox=tableRowBox + "/td[" + td[j] + "]/span[last()]";
                }else{
                    throw new Exception("出现特殊的结构");
                }*/
                tableColumnBox = tableRowBox + "/td[" + td[j] + "]/span/span";
                tableColumnBox = "(" + tableColumnBox + ")[last()]";
                // 若该行该列值不匹配，则 flag 置为 false
                if (!driver.findElement(By.xpath(tableColumnBox)).getText().contains(data[j])) {
                    System.out.println(driver.findElement(By.xpath(tableColumnBox)).getText() + "   ");
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * 判断有多少行数据
     */
    public int pirRowsOfTable(String boxType) throws Exception {
        // 外框定位
        String parentDiv = "";
        // table 中 row 定位
        String tableRowsBox = "";
        // 该页面 table 中 row 数量
        int tableRowsNum;
        // 如果匹配到 tab 标签页
        if (boxType.equals("tab")) {
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
        }
        // 如果匹配到 modal 模态框
        else if (boxType.equals("modal")) {
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-content']";
        }
        // 如果匹配到 slide 滑动框
        else if (boxType.equals("slide")) {
            log.info("现在匹配到滑动框");
            parentDiv = "//div[contains(@class,'slide-frame animated slideInRight')]";
        }
        // 或者什么都没匹配到
        else {
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }
        tableRowsBox = parentDiv + "//div[@class='ant-table-wrapper']//div[@class='ant-table-scroll']//table/tbody/tr";
        tableRowsNum = driver.findElements(By.xpath(tableRowsBox)).size();
        return tableRowsNum;
    }

    /**
     * 新建或者搜索中一系列的输入框进行输入
     * 注意：此方法为复杂方法，建议使用，通过传一个字符数组对所有可以更改的输入框进行输入
     * <p>
     * 与inputAllData不同点在于:
     * inputAllData中循环次数是以框的数量来循环的
     * inputAllDataByData中循环时以数据的长度来循环的
     * 适用于页面中框数量较多，但是需要输入的数据并不是那么多的页面 例如：费用政策 新建页面
     *
     * @param boxType    新建或者搜索框的类型，是滑动框 slide，还是模态框 modal，还是一个页面显示 tab
     * @param data       "" 表示为新建操作，有值表示搜索操作
     * @param keysToSend （非必传）可以传注入 Keys.ENTER 的键盘按键操作
     * @throws Exception 当输入的 boxType 类型有问题时候抛出
     */
    protected void inputAllDataByData(String boxType, String data[], CharSequence... keysToSend) throws Exception {
        /* ========== 多种类型框的 xpath 定位 ========== */
        // 外框 xpath 定位
        String parentDiv = "";
        // 里头块级元素的 xpath 定位
        String childDiv = "";
        // 下拉框（非默认勾选）
        String selectBox = "";
        // 普通输入框
        String inputBox = "";
        //多行文本输入框
        String textareaBox = "";
        // 开关按钮
        String switch1Box = "";
        // 开关按钮
        String switch2Box = "";
        // 日期输入框
        String dateBox = "";
        // 数量输入框
        String numBox = "";
        // 日期选择框
        String dateBox2 = "";
        // 弹出框
        String modalBox = "";
        /* ========== 判断弹出框是滑动框还是模态框还是标签框 ========== */
        // 如果匹配到 slide 滑动框
        if (boxType.equals("slide")) {
            log.info("现在匹配到滑动框");
            parentDiv = "//div[contains(@class,'slide-frame animated slideInRight')]";
            childDiv = parentDiv + "//div[contains(@class, 'ant-row ant-form-item')]";
        }
        // 如果匹配到 modal 模态框
        else if (boxType.equals("modal")) {
            log.info("现在匹配到模态框");
            parentDiv = "(//div[@class='ant-modal-content'])[last()]";
            childDiv = parentDiv + "//div[contains(@class,'ant-row ant-form-item')]";
        }
        // 如果匹配到 tab 标签框
        else if (boxType.equals("tab")) {
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
            childDiv = parentDiv + "//div[contains(@class, 'ant-row ant-form-item')]";
        }
        // 或者什么都没匹配到
        else {
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }
        /* ========== 遍历各个 div 模块查找其中各种类型元素 ========== */
        // 遍历框中这几个块级元素
        for (int i = 1; i <= data.length; i++) {
            // 下拉框
            selectBox = "(" + childDiv + ")[" + i + "]//div[contains(@class,'ant-select ant-select-enabled') and not(contains(@class,'ant-select-no-arrow'))]";
            // 普通输入框
            inputBox = "(" + childDiv + ")[" + i + "]//input[contains(@placeholder,'请输入') and not(@disabled)]";
            //多行文本输入框
            textareaBox = "(" + childDiv + ")[" + i + "]//textarea[contains(@placeholder,'请输入') and not(@disabled)]";
            // 开关按钮 禁用
            switch1Box = "(" + childDiv + ")[" + i + "]//button[@class='ant-switch']";
            // 开关按钮 启用
            switch2Box = "(" + childDiv + ")[" + i + "]//button[@class='ant-switch ant-switch-checked']";
            // 日期输入框
            dateBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-calendar-picker-input ant-input']";
            // 数量输入框
            numBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-input-number-input' and not(@disabled)]";
            // 日期选择框
            dateBox2 = "" + childDiv + "";
            // 下拉框在此块级元素中存在
            if (isElementContained(selectBox)) {
                // 点击下拉框
                clickButton(0, By.xpath(selectBox), 0);
                // Actions
                Actions actions = new Actions(driver);
                // controls
                String controls = driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]//span[@class='ant-form-item-children']/div/div")).getAttribute("aria-controls");
                // 等 10 s 直到下拉框中空白或者转圈消失
                for (int index = 0; index < 100; index++) {
                    if (!isElementContained("//div[@id='" + controls + "']//div[@class='ant-empty-image']") && !isElementContained("//div[@id='" + controls + "']//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                        break;
                    }
                    Thread.sleep(100);
                }
                // 如果数组中该文本数据为空，默认选取第一行
                if (data[i - 1].equals("")) {
                    actions.sendKeys(Keys.ENTER).perform();
                }
                // 若文本不为空
                else {
                    // 若是 tab
                    clickButton(0, By.xpath("//div[@id='" + controls + "']/ul/li[text()='" + data[i - 1] + "'] | //div[@id='" + controls + "']/ul/li//span[text()='" + data[i - 1] + "']"), 0);
                }
                // 下拉框没有回弹才去按下 ESC
                String selectBoxIn = selectBox + "/div[@aria-expanded='false']";
                if (!isElementContained(selectBoxIn) && !boxType.equals("modal")) {
                    // 避免一种 select 框选择行下拉框无法弹回
                    actions = new Actions(driver);
                    actions.sendKeys(Keys.ESCAPE).perform();
                }
            }
            // 若日期输入框在此块级元素中存在
            else if (isElementContained(dateBox)) {
                // 目前用点击后输入日期回车来处理
                clickButton(By.xpath(dateBox));
                String tab_input_xpath = "(" + childDiv + ")[" + i + "]//input[@class='ant-calendar-input ']";
                String slide_input_xpath = "//input[@class='ant-calendar-input ']";
                String modal_input_xpath = "(" + childDiv + ")[" + i + "]//input[contains(@class,'ant-calendar')]";
                if (isElementContained(tab_input_xpath)) {
                    if(!"".equals(driver.findElement(By.xpath(tab_input_xpath)).getAttribute("value"))){
                        driver.findElement(By.xpath(tab_input_xpath)).sendKeys(Keys.CONTROL,"a");
                        driver.findElement(By.xpath(tab_input_xpath)).sendKeys(Keys.BACK_SPACE);
                    }
                    inputData(By.xpath(tab_input_xpath), "".equals(data[i-1])? ClockTool.getTime():data[i-1]);
                } else if (isElementContained(slide_input_xpath)) {
                    if(!"".equals(driver.findElement(By.xpath(slide_input_xpath)).getAttribute("value"))){
                        driver.findElement(By.xpath(slide_input_xpath)).sendKeys(Keys.CONTROL,"a");
                        driver.findElement(By.xpath(slide_input_xpath)).sendKeys(Keys.BACK_SPACE);
                    }
                    inputData(By.xpath(slide_input_xpath), "".equals(data[i-1])?ClockTool.getTime():data[i-1]);
                }else if(isElementContained(modal_input_xpath)){
                    if(!"".equals(driver.findElement(By.xpath(modal_input_xpath)).getAttribute("value"))){
                        driver.findElement(By.xpath(modal_input_xpath)).sendKeys(Keys.CONTROL,"a");
                        driver.findElement(By.xpath(modal_input_xpath)).sendKeys(Keys.BACK_SPACE);
                    }
                    inputData(By.xpath(modal_input_xpath), "".equals(data[i-1])?ClockTool.getTime():data[i-1]);
                }
                else {
                    log.info("出现特殊的日期输入框");
                    /*throw new Exception("出现特殊的日期输入框");*/
                }
                Actions actions = new Actions(driver);
                actions.sendKeys(data[i - 1], Keys.ENTER).perform();
                Thread.sleep(1000);
            }
            // 若数量输入框在此块级元素中存在
            else if (isElementContained(numBox)) {
                // 如果传入值位""
                if (data[i - 1].equals("")) {
                    // 数量输入框若没有文本就用 1 来代替
                    if (driver.findElement(By.xpath(numBox)).getAttribute("value") == null || driver.findElement(By.xpath(numBox)).getAttribute("value").equals("")) {
                        inputData(By.xpath(numBox), "1");
                    }
                } else {
                    Actions actions = new Actions(driver);
                    actions.click(driver.findElement(By.xpath(numBox))).perform();
                    // 组合键清空
                    driver.findElement(By.xpath(numBox)).sendKeys(Keys.chord(Keys.CONTROL, "a"));
                    driver.findElement(By.xpath(numBox)).sendKeys(Keys.BACK_SPACE);
                    inputData(By.xpath(numBox), data[i - 1]);
                }
            }
            // 若常规输入框在此块级元素中存在
            else if (isElementContained(inputBox)) {
                // 点击输入框并输入数据
                inputData(By.xpath(inputBox), data[i - 1]);
            }
            //若多行文本输入框再此块级元素中存在
            else if (isElementContained(textareaBox)) {
                // 点击多行输入框并输入数据
                inputData(By.xpath(textareaBox), data[i - 1]);
            }
            // 若禁用开关按钮在此块级元素中存在
            else if (isElementContained(switch1Box)) {
                if ("启用".equals(data[i - 1])) {
                    // 点击开关按钮使之变为禁用
                    clickButton(By.xpath(switch1Box));
                }
            }
            // 若启用开关按钮在此块级元素中存在
            else if (isElementContained(switch2Box)) {
                if ("禁用".equals(data[i - 1])) {
                    // 点击开关按钮使之变为警用
                    clickButton(By.xpath(switch2Box));
                }

            }
        }
        // 若最后传了按键操作
        if (keysToSend.length != 0) {
            Actions actions = new Actions(driver);
            actions.sendKeys(keysToSend).perform();
            // 回车之后保证页面上所有的转圈加载是加载完的
            for (int i = 0; i < 300; i++) {
                if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                    break;
                }
                Thread.sleep(100);
            }
        }
    }


    /**
     * 类似于clickButton的方法
     * 不过去除了等待页面中转圈的效果
     *
     * @param beforeTime ms 时间表示点击之前等待多少时延
     * @param by         点击按钮的定位
     * @param afterTime  ms 时间表示点击之后等待多少延时
     */
    protected void clickButtonNoWait(long beforeTime, By by, long afterTime) throws Exception {
        // 点击之前延时
        Thread.sleep(beforeTime);
        // 等到页面加载存在并可见并且可点击
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        wait.until(ExpectedConditions.elementToBeClickable(by));
        WebElement buttonElement = driver.findElement(by);
        buttonElement.click();
        // 点击之后延时
        Thread.sleep(afterTime);
    }

    /**
     * 重写closeFirstTab方法，使之点击的时候不需要等待页面转圈
     *
     * @throws Exception 其他异常
     */
    protected void closeFirstTab() throws Exception {
        clickButtonNoWait(300, By.xpath("((//div[@class='ant-tabs-nav-scroll'])[1]/div//i)[last()]"), 0);
    }


    /**
     * 等待消息提示框的消息，用于费用预提单中，例如单据审批之后，会有消息提示框，所以等待几秒
     *
     * @throws Exception 其他异常
     */
    protected void wiatForMessageClose() throws Exception {
        String xpath_message = "//div[@class='ant-dropdown antd-pro-components-header-dropdown-index-container antd-pro-components-notice-icon-index-popover ant-dropdown-placement-bottomRight']//div[text()='消息']";
        for (int i = 0; i < 10; i++) {
            if (isElementContained(xpath_message)) {
                Thread.sleep(1000);
                continue;
            }
            break;
        }
    }


    /**
     * 用于单独搜索的判断
     * 搜索中会出现的框的类型 展示按照 下拉框、输入框、日期框来预设
     *
     * @param boxType    页面类型
     * @param data       搜索的数据
     * @param td         对应的表格中的位置
     * @param keysToSend 其他参数    0代表正常的表格 校验时就使用IsDataMatchTable
     *                   1代表不正常的表格 例如费用预提单 校验时使用pirIsDataMatchTable
     * @throws Exception 其他异常
     */
    protected boolean isAloneSearch(String boxType, String data[], int[] td, CharSequence... keysToSend) throws Exception {

        /* ========== 多种类型框的 xpath 定位 ========== */
        // 外框 xpath 定位
        String parentDiv = "";
        // 里头块级元素的 xpath 定位
        String childDiv = "";

        // 下拉框（非默认勾选）
        String selectBox = "";
        // 普通输入框
        String inputBox = "";
        // 日期输入框
        String dateBox = "";
        // 弹出框
        String modalBox = "";

        //搜索按钮
        String searchButton = "";
        //清空按钮
        String clearButton = "";

        /* ========== 判断弹出框是模态框还是标签框 ========== */
        // 如果匹配到 modal 模态框
        if (boxType.equals("modal")) {
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']";
            childDiv = parentDiv + "//div[contains(@class,'ant-row ant-form-item')]";
            searchButton = parentDiv + "//form//button[@class='ant-btn ant-btn-primary']";
            clearButton = parentDiv + "//form//button[@class='ant-btn']";
        }
        // 如果匹配到 tab 标签框
        else if (boxType.equals("tab")) {
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
            childDiv = parentDiv + "//div[contains(@class, 'ant-row ant-form-item')]";
            searchButton = parentDiv + "//form//button[@class='ant-btn ant-btn-primary']";
            clearButton = parentDiv + "//form//button[@class='ant-btn']";
        }
        // 或者什么都没匹配到
        else {
            log.info("你输入的弹框类型有误或者暂不支持！");

            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }

        /*点击更多按钮*/
        if("0".equals(keysToSend[1])){
            clickMoreIcon();
        }

        // 遍历框要输入数据的几个框
        for (int i = 1; i <= data.length; i++) {
            /**
             *1.先点击清空按钮
             * */
            clickButton(100, By.xpath(clearButton), 100);

            /**
             * 2.输入数据
             * */
            // 下拉框
            selectBox = "(" + childDiv + ")[" + i + "]//div[contains(@class,'ant-select ant-select-enabled') and not(contains(@class,'ant-select-no-arrow'))]";
            // 普通输入框
            inputBox = "(" + childDiv + ")[" + i + "]//input[contains(@placeholder,'请输入') and not(@disabled)]";
            // 日期输入框
            dateBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-calendar-picker-input ant-input']";

            // 下拉框在此块级元素中存在
            if (isElementContained(selectBox)) {
                // 点击下拉框
                clickButton(0, By.xpath(selectBox), 0);
                // Actions
                Actions actions = new Actions(driver);
                // controls
                String controls = driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]//span[@class='ant-form-item-children']/div/div")).getAttribute("aria-controls");
                // 等 10 s 直到下拉框中空白或者转圈消失
                for (int index = 0; index < 100; index++) {
                    if (!isElementContained("//div[@id='" + controls + "']//div[@class='ant-empty-image']") && !isElementContained("//div[@id='" + controls + "']//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                        break;
                    }
                    Thread.sleep(100);
                }
                // 如果数组中该文本数据为空，默认选取第一行
                if (data[i - 1].equals("")) {
                    actions.sendKeys(Keys.ENTER).perform();
                }
                // 若文本不为空
                else {
                    // 若是 tab
                    System.out.println("//div[@id='" + controls + "']/ul/li[text()='" + data[i - 1] + "'] | //div[@id='" + controls + "']/ul/li//span[text()='" + data[i - 1] + "']");
                    log.info("//div[@id='" + controls + "']/ul/li[text()='" + data[i - 1] + "'] | //div[@id='" + controls + "']/ul/li//span[text()='" + data[i - 1] + "']");
                    clickButton(0, By.xpath("//div[@id='" + controls + "']/ul/li[contains(text(),"+ data[i - 1]+")] | //div[@id='" + controls + "']/ul/li//span[contains(text()," + data[i - 1] +")]"), 0);
                }
                // 下拉框没有回弹才去按下 ESC
                String selectBoxIn = selectBox + "/div[@aria-expanded='false']";
                if (!isElementContained(selectBoxIn) && !boxType.equals("modal")) {
                    // 避免一种 select 框选择行下拉框无法弹回
                    actions = new Actions(driver);
                    actions.sendKeys(Keys.ESCAPE).perform();
                }
            }
            // 若日期输入框在此块级元素中存在
            else if (isElementContained(dateBox)) {
                // 目前用点击后输入日期回车来处理
                clickButton(By.xpath(dateBox));
                String tab_input_xpath = "(" + childDiv + ")[" + i + "]//input[@class='ant-calendar-input ']";
                String slide_input_xpath = "//input[@class='ant-calendar-input ']";
                if (isElementContained(tab_input_xpath)) {
                    inputData(By.xpath(tab_input_xpath), ClockTool.getTime());
                } else if (isElementContained(slide_input_xpath)) {
                    inputData(By.xpath(slide_input_xpath), ClockTool.getTime());
                } else {
                    log.info("出现特殊的日期输入框");
                    /*throw new Exception("出现特殊的日期输入框");*/
                }
                Actions actions = new Actions(driver);
                actions.sendKeys(data[i - 1], Keys.ENTER).perform();
                Thread.sleep(1000);
            }
            // 若常规输入框在此块级元素中存在
            else if (isElementContained(inputBox)) {
                // 点击输入框并输入数据
                inputData(By.xpath(inputBox), data[i - 1]);
            }
            // 若上面这些类型的框都不存在
            else{
                log.info("第"+i+"个框不符合要求");
                continue;
            }
            /**
             * 3.点击搜索
             * */
            clickButton(100, By.xpath(searchButton), 100);

            /**
             * 4.断言搜索结果是否符合条件
             * */
            // 回车之后保证页面上所有的转圈加载是加载完的
            for (int j = 0; j < 300; j++) {
                if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                    break;
                }
                Thread.sleep(100);
            }
            String[] data_dis = new String[]{data[i - 1]};
            int[] td_dis = new int[]{td[i - 1]};
            if ("0".equals(keysToSend[0])) {
                if (isDataMatchTable(boxType, data_dis, td_dis)) {
                    continue;
                } else {
                    return false;
                }
            } else if ("1".equals(keysToSend[0])) {
                if (pirIsDataMatchTable(boxType, data_dis, td_dis)) {
                    continue;
                } else {
                    return false;
                }
            } else {
                log.info("传入错误的参数");
                throw new Exception("传入错误的参数");
            }
        }
        return true;
    }


    /**
     * 新建功能中 点击申请类型/报销类型 之后的弹出框数据的填写
     *
     * @param data 填写的数据
     * @throws Exception 出现特殊的框导致的异常
     */
    protected void inputAllDataDemension(String[] data) throws Exception {

        /* 父级标签 parentDiv，即为弹出框div的xpath*/
        String parentDiv = "(//div[@class='ant-modal-content']//div[@class='ant-modal-body'])[last()]";

        /*子级标签  selectDiv 大类的下拉框*/
        String selectDiv = parentDiv + "//div[@id='typeCategoryId']";

        /*子级标签 nameInput 右侧的名称输入框*/
        String nameInput = parentDiv + "//input[@id='name']";

        /*子级标签 右侧搜索小图标*/
        String searchButton = parentDiv + "//button[@type='submit']";

        /*子级标签 搜索出来的结果*/
        String resultDiv = "(" + parentDiv + "//div[@class='type-item '])[last()]";

        /*子级标签 确定按钮*/
        String defineButton = "(//div[@class='ant-modal-footer']//button[@class='ant-btn ant-btn-primary'])[last()]";

        /*子级标签 取消按钮*/
        String cancelButton = "(//div[@class='ant-modal-footer']//button[@class='ant-btn'])[last()]";

        /*点击下拉框并选中相关数据*/
        clickButton(0, By.xpath(selectDiv), 0);
        if (data[0].equals("")) {
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ENTER).perform();
        } else {
            String controls = driver.findElement(By.xpath(selectDiv + "/div")).getAttribute("aria-controls");
            clickButton(0, By.xpath("//div[@id='" + controls + "']/ul/li[text()='" + data[0] + "']"), 100);
        }
        /*输入框输入数据*/
        inputData(By.xpath(nameInput), data[1]);
        /*点击搜索小图标*/
        clickButton(0, By.xpath(searchButton), 300);

        /*判断有无搜索出来的数据,有则点击数据后点击确定  无则点击取消*/
        if (isElementContained(resultDiv)) {
            clickButton(0, By.xpath(resultDiv), 0);
            clickButton(0, By.xpath(defineButton), 0);
        } else {
            clickButton(By.xpath(cancelButton));
        }

    }

    /**
     * 点击的方法(点击的元素需要移动到某个元素上才能点击)
     *
     * @param moveElement  移动到的元素的xpath
     * @param clickElement 点击元素的xpath
     * @throws Exception 其他异常
     */
    protected void clickByMove(By moveElement, By clickElement) {
        Actions actions = new Actions(driver);
        /*移动到moveElement元素上*/
        actions.moveToElement(driver.findElement(moveElement));
        WebElement webElement = driver.findElement(clickElement);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", webElement);
    }

    /**
     * 维度设置页面 填写数据的方法
     *
     * @param data       填写的数据
     * @param lineNumber 填写第几行的数据
     * @throws Exception 出现特殊的框导致的异常
     */
    public void inputAllDataDemension(String[] data, int lineNumber) throws Exception {
        /* 父级标签 parentDiv，即为需要添加数据的那一行的xpath */
        String parentDiv = "//div[@class='ant-table ant-table-small ant-table-bordered ant-table-scroll-position-left']//tbody//tr[" + lineNumber + "]";

        for (int i = 1; i <= data.length; i++) {
            /*下拉框*/
            String selectBox = parentDiv + "/td[" + i + "]" + "//div[@class='ant-select ant-select-enabled']";

            /*输入框*/
            String inputBox = parentDiv + "/td[" + i + "]" + "//div[@class='ant-input-number']//input";

            /*下拉框1*/
            String clearBox = parentDiv + "/td[" + i + "]" + "//div[@class='ant-select ant-select-enabled ant-select-allow-clear']";

            /*确定禁止输入框*/
            String disabledBox = parentDiv + "/td[" + i + "]" + "//div[@class='ant-select ant-select-disabled']";


            if (isElementContained(selectBox)) {
                /*是第一种下拉框的情况 点击下拉框*/
                clickButton(0, By.xpath(selectBox), 600);
                if ("".equals(data[i - 1])) {
                    /*当数据为空字符串的时候，直接点击回车选择第一个即可*/
                    Actions actions = new Actions(driver);
                    actions.sendKeys(Keys.ENTER).perform();
                } else {
                    String controls = driver.findElement(By.xpath(selectBox + "/div")).getAttribute("aria-controls");
                    clickButton(0, By.xpath("//div[@id='" + controls + "']/ul/li[text()='" + data[i - 1] + "']"), 100);
                }
            } else if (isElementContained(clearBox)) {
                /*是第二种下拉框的情况 点击下拉框*/
                clickButton(0, By.xpath(clearBox), 600);
                if ("".equals(data[i - 1])) {
                    /*当数据为空字符串的时候，直接点击回车选择第一个即可*/
                    Actions actions = new Actions(driver);
                    actions.sendKeys(Keys.ENTER).perform();
                } else {
                    String controls = driver.findElement(By.xpath(clearBox + "/div")).getAttribute("aria-controls");
                    clickButton(0, By.xpath("//div[@id='" + controls + "']/ul/li[text()='" + data[i - 1] + "']"), 100);
                }
            } else if (isElementContained(inputBox)) {
                //是输入框的情况
                inputData(By.xpath(inputBox), data[i - 1]);
            } else if (isElementContained(disabledBox)) {
                log.info("禁止输入框，啥也不做");
            } else {
                log.info("出现了除下拉框和输入框之外的框！");
            }

        }
    }

    /**
     * 页码选择操作
     *
     * @param value 参数一（非必传）：boxType 可传可不传，tab，slide，modal，传的话定位更准确一些
     *              参数二：rowsPerPage 多少条数据/每页，为 "" 表示默认当前
     *              参数三：page 第几页，此处可以为 ""，表示 input 框不去输入
     * @throws Exception 跳转第几页的 input 不允许为空
     */
    public void choosePageNum(String... value) throws Exception {
        // 页码所在框的类型，默认是 tab
        String boxType = "";
        // 多少条数据/每页 的文本
        String rowsPerPage;
        // 第几页的文本
        String page;
        // 若参数传了 null
        if (value != null) {
            // 若没有传参
            if (value.length != 2 && value.length != 3) {
                throw new Exception("参数要么传 2 个要么传 3 个值！");
            }
            // 若传了 2 个值
            else if (value.length == 2) {
                rowsPerPage = value[0];
                page = value[1];
            }
            // 若传了 3 个值
            else {
                boxType = value[0];
                rowsPerPage = value[1];
                page = value[2];
            }
        }
        // 否则抛出参数没传的异常
        else {
            throw new Exception("参数不允许为 null！");
        }

        // 父框 div，默认是 tab
        String parentDiv = "";
        // 如果匹配到 slide 滑动框
        if (boxType.equals("slide")) {
            log.info("现在匹配到滑动框");
            parentDiv = "//div[contains(@class,'slide-frame animated slideInRight')]";
        }
        // 如果匹配到 modal 模态框
        else if (boxType.equals("modal")) {
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']";
        }
        // 如果匹配到 tab 标签框
        else if (boxType.equals("tab")) {
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
        }
        // 或者什么都没匹配到
        else if (!boxType.equals("")) {
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }

        // 定位到选择每页多少条的下拉框
        String selectBox = "(" + parentDiv + "//ul[@class='ant-pagination ant-table-pagination mini']/li[last()]/div[1]/div)[last()]";
        // 定位到下拉框是哪一个 li
        String liBox;
        // 定位到跳转到哪一页的 input 框
        String inputBox = "(" + parentDiv + "//ul[@class='ant-pagination ant-table-pagination mini']/li[last()]/div[2]/input)[last()]";
        // 只有当 rowsPerPage 不为 "" 或者不为 null 时候才去选择每页多少条数据，否则使用默认
        if (rowsPerPage != null && !rowsPerPage.equals("")) {
            liBox = "(" + parentDiv + "//ul[@class='ant-pagination ant-table-pagination mini']/li[last()]/div//ul/li[text()='" + rowsPerPage + "'])[last()]";
            // 点击选择多少条的下拉框
            clickButton(0, By.xpath(selectBox), 500);
            // 点击指定的 li
            clickButton(0, By.xpath(liBox), 500);
        }
        // input 中输入需要跳转第几页，其传值可以为 ""，但是不允许为 null
        if (page == null) {
            throw new Exception("跳转第几页的 input 不允许为 null");
        }
        // 如果 input 框存在，就点击跳转到第几页的 input 框再按下回车
        if (isElementContained(inputBox)) {
            inputData(By.xpath(inputBox), page, Keys.ENTER);
            Thread.sleep(500);
        }
    }

    /**
     * 判断搜索框中数据是否被清空，返回布尔类型
     *
     * @param boxType 新建或者搜索框的类型，是滑动框 slide，还是模态框 modal，还是一个页面显示 tab
     * @return 返回各个框中数据是否被清空的布尔类型
     * @throws Exception 当输入的 boxType 类型有问题时候抛出
     */
    protected boolean isCleared(String boxType,String liBoxText) throws Exception {
        /* ========== 多种类型框的 xpath 定位 ========== */
        // 返回的布尔值
        boolean flag = true;
        // 外框 xpath 定位
        String parentDiv = "";
        // 里头块级元素的 xpath 定位
        String childDiv = "";
        // 里头块级元素个数
        int divNum = 0;
        // 下拉框（非默认勾选）
        String selectBox = "";
        // 普通输入框
        String inputBox = "";
        // 开关按钮
        String switchBox = "";
        // 日期输入框
        String dateBox = "";
        // 数量输入框
        String numBox = "";
        // 日期选择框
        String dateBox2 = "";
        // 弹出框
        String modalBox = "";
        /* ========== 判断弹出框是滑动框还是模态框还是标签框 ========== */
        // 如果匹配到 slide 滑动框
        if (boxType.equals("slide")) {
            log.info("现在匹配到滑动框");
            parentDiv = "//div[contains(@class,'slide-frame animated slideInRight')]";
            childDiv = parentDiv + "//div[@class='ant-row ant-form-item']";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 如果匹配到 modal 模态框
        else if (boxType.equals("modal")) {
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-content']";
            childDiv = parentDiv + "//div[@class='ant-row ant-form-item']";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 如果匹配到 tab 标签框
        else if (boxType.equals("tab")) {
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
            childDiv = parentDiv + "//div[@class='ant-row ant-form-item']";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 或者什么都没匹配到
        else {
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }
        /* ========== 遍历各个 div 模块查找其中各种类型元素 ========== */
        // 遍历框中这几个块级元素
        for (int i = 1; i <= divNum; i++) {
            // 下拉框
            selectBox = "(" + childDiv + ")[" + i + "]//div[contains(@class,'ant-select ant-select-enabled')]";
            // 普通输入框
            inputBox = "(" + childDiv + ")[" + i + "]//input[contains(@placeholder,'请输入') and not(@disabled)]";
            // 开关按钮
            switchBox = "(" + childDiv + ")[" + i + "]//button[@class='ant-switch']";
            // 日期输入框
            dateBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-calendar-picker-input ant-input']";
            // 数量输入框
            numBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-input-number-input']";
            // 日期选择框
            dateBox2 = "" + childDiv + "";
            // 下拉框在此块级元素中存在
            if (isElementContained(selectBox)) {
                // 如果是不被允许清空的下拉框，即判断有一个红色的星号
                if (driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class") != null && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class").contains("ant-form-item-required")) {
                    String text = driver.findElement(By.xpath(selectBox + "//div[@class='ant-select-selection-selected-value']")).getText();
                    if (!text.equals(liBoxText)) {
                        flag = false;
                        break;
                    }
                    Actions actions = new Actions(driver);
                    actions.sendKeys(Keys.ESCAPE).perform();
                    Thread.sleep(500);
                }
                // 如果是被允许清空的下拉框
                else {
                    if (isElementContained(selectBox + "//div[@class='ant-select-selection-selected-value']")) {
                        flag = false;
                        break;
                    }
                }
            }
            // 若常规输入框在此块级元素中存在
            else if (isElementContained(inputBox)) {
                if (!(driver.findElement(By.xpath(inputBox)).getText().equals("") || driver.findElement(By.xpath(inputBox)).getText() == null)) {
                    flag = false;
                    break;
                }
            }
            // 若日期输入框在此块级元素中存在
            else if (isElementContained(dateBox)) {
                if (!(driver.findElement(By.xpath(dateBox)).getText().equals("") || driver.findElement(By.xpath(dateBox)).getText() == null)) {
                    flag = false;
                    break;
                }
            }
            // 若数量输入框在此块级元素中存在
            else if (isElementContained(numBox)) {
                if (!(driver.findElement(By.xpath(numBox)).getText().equals("") || driver.findElement(By.xpath(numBox)).getText() == null)) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

}
