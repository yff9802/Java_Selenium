package com.yff.test.common;

import com.yff.util.ClockTool;
import com.yff.util.ExcelTool;
import com.yff.util.FileTool;
import com.yff.util.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author YFF
 * @date 2020/4/4
 */
@Slf4j
public class PageCommon {
    /**
     * 驱动
     */
    protected WebDriver driver;

    /**
     * 驱动等待
     */
    protected WebDriverWait wait;

    // 环境，表示环境是生产还是 uat
    public String environment;
    // 语言 zh_CN  en_GB 对应着简体，英文
    public String language;

    // 数据驱动数组下标，num1 表示是生产还是 uat，num2 表示是中文还是英文
    public int num1;
    public int num2;


    /**
     * 构造器 根据环境参数和语言参数的值，设定常数num1、num2的值
     * @param driver   驱动
     * @param language 语言
     */
    public PageCommon(WebDriver driver, String environment, String language) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 20);
        this.environment = environment;
        this.language = language;
        if (environment.equals("prod")) {
            num1 = 0;
            if (language.equals("zh_CN")) {
                num2 = 0;
            } else if (language.equals("en_GB")) {
                num2 = 1;
            }
        } else if (environment.equals("uat")) {
            num1 = 1;
            if (language.equals("zh_CN")) {
                num2 = 0;
            } else if (language.equals("en_GB")) {
                num2 = 1;
            }
        }

    }



    /** =================================== 页面基本操作 =================================== */
    /**
     * 通过点击进行指定页面
     * 导航栏搜索框处输入值后，点击搜索按钮，并进入指定页面
     **/
    protected void jumpByClick(String functionName) throws Exception {
        String input_xpath = "//form[@class='ant-form ant-form-horizontal']//input[@class='ant-input']";
        inputData(By.xpath(input_xpath), functionName);
        String search_xpath = "(//form[@class='ant-form ant-form-horizontal']//i)[last()]";
        clickButton(100, By.xpath(search_xpath), 100);
        String function_a = "//ul[@class='ant-menu ant-menu-dark ant-menu-root ant-menu-inline']//li[1]/a";
        clickButton(100, By.xpath(function_a), 100);
        closeDashBoardTab();
    }

    /**
     * 通过 url 进行页面跳转
     *
     * @param url 指定功能模块的 url
     */
    protected void jumpByUrl(String url) {
        // 通过 url 进行跳转
        driver.get(url);
    }

    /**
     * 滑动到最顶上
     */
    protected void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
    }

    /**
     * 滑动到最低端
     */
    protected void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }


    /**
     * 滚动滚动条，让元素顶端和页面顶端对齐
     *
     * @param by 需要对齐的元素
     */
    protected void scrollElementTopToTop(By by) {

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(by));
    }

    /**
     * 滚动滚动条，让元素底端和页面底端对齐
     *
     * @param by 需要对齐的元素
     */
    protected void scrollElementBottomToBottom(By by) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", driver.findElement(by));
    }

    protected void scrollElementView(By by){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",driver.findElement(by));
    }

    /**
     * 点击顶上的可关闭的第一个 tab
     */
    protected void closeFirstTab() throws Exception {
        clickButton(300, By.xpath("((//div[@class='ant-tabs-nav-scroll'])[1]/div//i)[last()]"), 0);
    }

    /**
     * 用于叉掉页面中的仪表盘  原因：仪表盘中存在tbody，会导致相关定位不准确
     * 使用场景：
     *      1.在Page层中jumpToPageModule中调用
     *      2.在刷新页面后，会自动出现仪表盘
     * @throws Exception 点击不到异常
     */
    protected void closeDashBoardTab()throws Exception{
        if(isElementContained("//span[text()='仪表盘' and @class='ant-dropdown-trigger']")){
            clickButton(100,By.xpath("//span[text()='仪表盘' and @class='ant-dropdown-trigger']/../..//i"),100);
        }
    }

    /**
     * 获取顶上的最新的可关闭的 tab的名称
     * 用途：常用语新建取消之后是否返回到一级界面的判断，由于URL的判断方式并不好用
     */
    protected String getFirstTabName() {
        return driver.findElement(By.xpath("(//header/../..//div[contains(@class,'ant-tabs-nav-scroll')]/div/div/div)[last()]//span[@class='ant-dropdown-trigger']")).getText();
    }


    /**
     * 获取页面条显示的数据条数
     *
     * @param by 显示页面条数的xpath ，例如 显示 1 - 10 共 189 条的xapth
     * @return 返回数据条数
     * @throws Exception 出现找不到页码条数据的异常
     */
    protected Integer getPageNumber(By by) throws Exception {
        String pageNumText = driver.findElement(by).getText();
        String[] pageNumChar = pageNumText.split(" ");
        return Integer.parseInt(pageNumChar[3]);
    }

    protected Integer getPageNumber(By by,int i) throws Exception {
        String pageNumText = driver.findElement(by).getText();
        String[] pageNumChar = pageNumText.split(" ");
        return Integer.parseInt(pageNumChar[i]);
    }

    /**
     * 切换窗口句柄
     *
     * @param i 表示第几个句柄，i 从 1 开始
     */
    protected void switchHandle(int i) {
        // 当前窗口句柄
        String handle = driver.getWindowHandle();
        // 当前所有窗口句柄
        Set<String> alllHandlesSet = driver.getWindowHandles();
        // 转成 ArrayList
        List<String> allHandlesList = new ArrayList<>(alllHandlesSet);
        // 切换句柄
        driver.switchTo().window(allHandlesList.get(i - 1));
    }

    /** =================================== 基本元素操作 =================================== */
    /**
     * 点击按钮操作，封装延时 500 ms
     *
     * @param by 点击按钮的定位
     */
    protected void clickButton(By by) throws Exception {
        // 点击之前保证页面上所有的转圈加载是加载完的
        int i;
        for (i = 0; i < 300; i++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
        // 转圈超时异常
        if (i == 300) {
            throw new Exception("页面加载转圈超时异常！");
        }
        // 等到页面加载存在并可见并且可点击
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        wait.until(ExpectedConditions.elementToBeClickable(by));
        WebElement buttonElement = driver.findElement(by);
        buttonElement.click();
        // 点击之后保证页面上所有的转圈加载是加载完的
        for (int j = 0; j < 300; j++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
    }

    /**
     * 点击按钮操作，外部延时传参
     *
     * @param beforeTime ms 时间表示点击之前等待多少时延
     * @param by         点击按钮的定位
     * @param afterTime  ms 时间表示点击之后等待多少延时
     */
    protected void clickButton(long beforeTime, By by, long afterTime) throws Exception {
        // 点击之前延时
        Thread.sleep(beforeTime);
        // 点击之前保证页面上所有的转圈加载是加载完的
        for (int i = 0; i < 300; i++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
        // 等到页面加载存在并可见并且可点击
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        wait.until(ExpectedConditions.elementToBeClickable(by));
        WebElement buttonElement = driver.findElement(by);
        buttonElement.click();
        // 点击之后保证页面上所有的转圈加载是加载完的
        for (int i = 0; i < 300; i++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
        // 点击之后延时
        Thread.sleep(afterTime);
    }

    /**
     * input 框出现后就去点击先清空然后输入
     *
     * @param by         input 的 By 定位
     * @param keysToSend 要输入的数据
     */
    protected void inputData(By by, CharSequence... keysToSend) throws Exception {
        // 点击之前保证页面上所有的转圈加载是加载完的
        for (int i = 0; i < 300; i++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
        // 等到页面加载存在 input 并且可见并且可点击
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        wait.until(ExpectedConditions.elementToBeClickable(by));
        WebElement inputElement = driver.findElement(by);
        Actions actions = new Actions(driver);
        actions.click(inputElement).perform();
        // 清空 input 并且进行数据输入
        inputElement.clear();
        // 输入数据
        inputElement.sendKeys(keysToSend);
        // 点击之后保证页面上所有的转圈加载是加载完的
        for (int i = 0; i < 300; i++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
    }

    /**
     * input 框出现后就去点击先清空然后输入
     *
     * @param element    input 的元素
     * @param keysToSend 要输入的数据
     */
    protected void inputData(WebElement element, CharSequence... keysToSend) throws Exception {
        // 点击之前保证页面上所有的转圈加载是加载完的
        for (int i = 0; i < 300; i++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
        // 等到页面加载存在 input 并且可见并且可点击
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
        // 清空 input 并且进行数据输入
        element.clear();
        // 填写值
        element.sendKeys(keysToSend);
        // 点击之后保证页面上所有的转圈加载是加载完的
        for (int i = 0; i < 300; i++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
    }

    /**
     * 下拉框点击并选中所需要的选项
     *
     * @param by    下拉框定位
     * @param ulBy  下拉列表 ul 定位
     * @param liStr 下拉列表中需要选中的一行中的文本的元素定位
     */
    protected void selectDropBox(By by, By ulBy, String liStr) throws Exception {
        // 等到下拉框存在并可见并可被点击
        clickButton(by);
        // 等到下拉列表(ul)出现了，就在此列表(ul)中进行滑动到顶端的操作
        wait.until(ExpectedConditions.visibilityOfElementLocated(ulBy));
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(ulBy)).perform();
//        scrollElementTopToTop(By.xpath(liStr));
//         等到需要选中的 li 可见并且可被点击时候进行操作
        clickButton(By.xpath(liStr));
    }

    /**
     * 此方法用来判断一个红色星号的输入框在其中没有文本时候，点击保存之后其样式是否正常显示，可以与 isAllDataAllowedNull 方法相比较
     * 该方法测单个框，isAllDataAllowedNull 测试所有的框的样式在点击保存后是否合规
     * ps：元素定位到需要断言的框，但是请定位到 //div[@class='ant-row ant-form-item'] 这一层！！！！！！！！
     *
     * @param itemLocator 用来定位到该框，只需要定位到 //div[@class='ant-row ant-form-item']
     * @return 返回该标有红色星号且无文本的输入框在点击保存后样式是否正常的布尔值
     */
    protected boolean isDataAllowedNull(String itemLocator) {
        // 返回的布尔值
        boolean flag = true;
        // 里头块级元素个数
        int divNum = 0;
        // 若该框带有红色星号
        if (driver.findElement(By.xpath(itemLocator)).getAttribute("class") != null && driver.findElement(By.xpath(itemLocator)).getAttribute("class").contains("ant-form-item-required")) {
            // 下拉框定位
            String selectBox = itemLocator + "//div[contains(@class,'ant-select ant-select-enabled')]";
            // 非下拉框
            String inputBox = itemLocator + "//input[not(@disabled)]";
            // 如果是下拉框
            if (isElementContained(selectBox)) {
                // 若框中无文本值
                if (!(driver.findElement(By.xpath(selectBox + "//div[@class='ant-select-selection-selected-value']")).getText().equals("") ||
                        driver.findElement(By.xpath(selectBox + "//div[@class='ant-select-selection-selected-value']")).getText() == null)) {
                    // 若框的样式不对
                    if (!driver.findElement(By.xpath(itemLocator + "/div[2]/div[1]")).getAttribute("class").contains("has-error")) {
                        flag = false;
                    }
                }
            }
            // 如果是非下拉框
            else {
                // 若框中无文本
                if (!(driver.findElement(By.xpath(inputBox)).getText().equals("") ||
                        driver.findElement(By.xpath(inputBox)).getText() == null)) {
                    // 若框的样式不对
                    if (!driver.findElement(By.xpath(itemLocator + "/div[2]/div[1]")).getAttribute("class").contains("has-error")) {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    /** =================================== 简单共用方法操作 =================================== */
    /**
     * 判断元素是否存在，在页面加载完全后可用
     *
     * @param str 元素 xpath 定位
     * @return 返回元素是否存在的布尔值
     */
    protected boolean isElementContained(String str) {
        // 将隐式等待关闭，或者设置成非常小
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean flag = true;
        try {
            // 若找不到元素此处会抛出异常
            driver.findElement(By.xpath(str));
        } catch (Exception e) {
            // 若抛出异常，即找不到元素
            flag = false;
        } finally {
            // 还原隐式等待延时
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            return flag;
        }
    }

    /**
     * 判断元素是否存在，在页面加载完全后可用
     *
     * @param by 元素 xpath 定位
     * @return 返回元素是否存在的布尔值
     */
    protected boolean isElementContained(By by) {
        // 将隐式等待关闭，或者设置成非常小
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean flag = true;
        try {
            // 若找不到元素此处会抛出异常
            driver.findElement(by);
        } catch (Exception e) {
            // 若抛出异常，即找不到元素
            flag = false;
        } finally {
            // 还原隐式等待延时
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            return flag;
        }
    }

    /**
     * 新建或者搜索中一系列的输入框进行输入
     * 注意：此方法为复杂方法，建议使用，通过传一个字符数组对所有可以更改的输入框进行输入
     *
     * @param boxType    新建或者搜索框的类型，是滑动框 slide，还是模态框 modal，还是一个页面显示 tab
     * @param data       "" 表示为新建操作，有值表示搜索操作
     * @param keysToSend （非必传）可以传注入 Keys.ENTER 的键盘按键操作
     * @throws Exception 当输入的 boxType 类型有问题时候抛出
     */
    protected void inputAllData(String boxType, String data[], CharSequence... keysToSend) throws Exception {
        /* ========== 多种类型框的 xpath 定位 ========== */
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
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 如果匹配到 modal 模态框
        else if (boxType.equals("modal")) {
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']";
            childDiv = parentDiv + "//div[contains(@class,'ant-row ant-form-item')]";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // 如果匹配到 tab 标签框
        else if (boxType.equals("tab")) {
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
            childDiv = parentDiv + "//div[contains(@class, 'ant-row ant-form-item')]";
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
                Thread.sleep(1500);
            }
            // 若数量输入框在此块级元素中存在
            else if (isElementContained(numBox)) {
               /* driver.findElement(By.xpath(numBox)).sendKeys(Keys.CONTROL, "a");
                driver.findElement(By.xpath(numBox)).sendKeys(Keys.BACK_SPACE);*/
                inputData(By.xpath(numBox), data[i - 1]);
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
     * 使用场景：比如说新建时候，很多数据都没有输入，然后点击保存之后可以调用此方法，此方法可判断带有星号未输数据的输入框在点击
     * 保存之后框的样式是否变成 "has-error" 的红色样式，只要有一个不满足该方法就会返回 false，当在未输入数据保存后，全部带有星
     * 号的框的样式都是正确的，该方法就会返回 ture
     * ps：该方法仅做判断框的样式是否合规，返回布尔值，方法前面一步需要自己点击保存按钮
     *
     * @param boxType 新建或者搜索框的类型，是滑动框 slide，还是模态框 modal，还是一个页面显示 tab
     * @return 全部带有红色星号且没有数据的输入框样式都是红框 "has-error" 就会返回 true，否则返回 false
     * @throws Exception 当输入的 boxType 类型有问题时候抛出
     */
    protected boolean isAllDataAllowedNull(String boxType) throws Exception {
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
            childDiv = parentDiv + "//form/div[contains(@class, 'ant-row ant-form-item')]";
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
            childDiv = parentDiv + "//div[contains(@class, 'ant-row ant-form-item')]";
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
            // 模态框
            modalBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-input']";
            // 日期输入框
            dateBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-calendar-picker-input ant-input']";
            // 数量输入框
            numBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-input-number-input']";
            // 普通输入框
            inputBox = "(" + childDiv + ")[" + i + "]//input[contains(@placeholder,'请输入') and not(@disabled)]";
            // 开关按钮
            switchBox = "(" + childDiv + ")[" + i + "]//button[@class='ant-switch']";
            // 日期选择框
            dateBox2 = "" + childDiv + "";
            // 下拉框在此块级元素中存在
            if (isElementContained(selectBox)) {
                // 如果是不被允许清空的下拉框，即判断有一个红色的星号
                if (isElementContained("(" + childDiv + ")[" + i + "]/div[1]/label") && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class") != null && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class").contains("ant-form-item-required")) {
                    String controls = driver.findElement(By.xpath(selectBox + "/div[1]")).getAttribute("aria-controls");
                    // 若存在此定位
                    if (isElementContained(selectBox + "//div[@class='ant-select-selection-selected-value']")) {
                        String text = driver.findElement(By.xpath(selectBox + "//div[@class='ant-select-selection-selected-value']")).getText();
//                    clickButton(0, By.xpath(selectBox), 500);
                        // 若下拉框中文本值没有
                        if (text.equals("")) {
                            // 若框的样式不对
                            if (!driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[2]/div[1]")).getAttribute("class").contains("has-error")) {
                                flag = false;
                                break;
                            }
                        }
                    }
                }
            }
            // 若常规输入框在此块级元素中存在
            else if (isElementContained(inputBox)) {
                // 若当前框前面有红色星号
                if (isElementContained("(" + childDiv + ")[" + i + "]/div[1]/label") && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class") != null && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class").contains("ant-form-item-required")) {
                    // 若框中文本值为空
                    if (!(driver.findElement(By.xpath(inputBox)).getText().equals("") || driver.findElement(By.xpath(inputBox)).getText() == null)) {
                        // 若框的样式不对
                        if (!driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[2]/div[1]")).getAttribute("class").contains("has-error")) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
            // 若模态框在此块级元素中存在
            else if (isElementContained(modalBox)) {
                // 若当前框前面有红色星号
                if (isElementContained("(" + childDiv + ")[" + i + "]/div[1]/label") && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class") != null && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class").contains("ant-form-item-required")) {
                    // 若框中文本值为空
                    if (!(driver.findElement(By.xpath(modalBox)).getText().equals("") || driver.findElement(By.xpath(modalBox)).getText() == null)) {
                        // 若框的样式不对
                        if (!driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[2]/div[1]")).getAttribute("class").contains("has-error")) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
            // 若日期输入框在此块级元素中存在
            else if (isElementContained(dateBox)) {
                // 若当前框前面有红色星号
                if (isElementContained("(" + childDiv + ")[" + i + "]/div[1]/label") && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class") != null && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class").contains("ant-form-item-required")) {
                    // 若框中文本值为空
                    if (!(driver.findElement(By.xpath(dateBox)).getText().equals("") || driver.findElement(By.xpath(dateBox)).getText() == null)) {
                        // 若框的样式不对
                        if (!driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[2]/div[1]")).getAttribute("class").contains("has-error")) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
            // 若数量输入框在此块级元素中存在
            else if (isElementContained(numBox)) {
                // 若当前框前面有文本并且是红色星号
                if (isElementContained("(" + childDiv + ")[" + i + "]/div[1]/label") && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class") != null && driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[1]/label")).getAttribute("class").contains("ant-form-item-required")) {
                    // 若框中文本值为空
                    if (!(driver.findElement(By.xpath(numBox)).getText().equals("") || driver.findElement(By.xpath(numBox)).getText() == null)) {
                        // 若框的样式不对
                        if (!driver.findElement(By.xpath("(" + childDiv + ")[" + i + "]/div[2]/div[1]")).getAttribute("class").contains("has-error")) {
                            flag = false;
                            break;
                        }
                    }
                }

            }
        }
        return flag;
    }

    /**
     * 新建空值校验之后是否有提示
     *
     * @return 返回各个框空值提示的布尔类型
     * @throws Exception 当输入的boxType类型有问题时候抛出
     */
    protected boolean isPromptContained(String boxType) throws Exception {
        /*等待几秒*/
        Thread.sleep(2000);
        /* ========== 多种类型框的 xpath 定位 ========== */
        // 外框 xpath 定位
        String parentDiv = "";
        // 里头块级元素的 xpath 定位
        String childDiv = "";
        //校验是否为必输字段label的xpath定位
        String judgeLabel = "";
        // 里头块级元素个数
        int divNum = 0;

        //下拉框
        String selectDisabledBox = "";
        // 下拉框（非默认勾选）
        String selectBox = "";
        // 普通输入框
        String inputBox = "";
        // 日期输入框
        String dateBox = "";
        // 数量输入框
        String numBox = "";

        /* ========== 判断弹出框是滑动框还是模态框还是标签框 ========== */
        // slide 滑动框
        if (boxType.equals("slide")) {
            log.info("现在匹配到滑动框");
            parentDiv = "//div[contains(@class,'slide-frame animated slideInRight')]";
            childDiv = parentDiv + "//div[contains(@class,'ant-row ant-form-item')]";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // modal 模态框
        else if (boxType.equals("modal")) {
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-content']";
            childDiv = parentDiv + "//div[contains(@class,'ant-row ant-form-item')]";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        // tab 标签框
        else if (boxType.equals("tab")) {
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
            childDiv = parentDiv + "//div[contains(@class,'ant-row ant-form-item')]";
            divNum = driver.findElements(By.xpath(childDiv)).size();
        }
        //其他情况
        else {
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }

        /* ========== 遍历各个 div 模块查找其中各种类型元素 ========== */
        // 遍历框中这几个块级元素
        for (int i = 1; i <= divNum; i++) {
            /**
             * 判断对应的块级元素是否为必输字段
             * 查看对应的label是否为class属性为 ant-form-item-required
             * */
            judgeLabel = "(" + childDiv + ")[" + i + "]//label[@class='ant-form-item-required']";
            if (!isElementContained(judgeLabel)) {
                continue;
            }

            /*定义每个框的定位*/
            String childrenDiv = "(" + childDiv + ")[" + i + "]";

            //下拉框 不能选择的
            selectDisabledBox = "(" + childDiv + ")[" + i + "]//div[contains(@class,'ant-select ant-select-disabled')]";
            // 下拉框
            selectBox = "(" + childDiv + ")[" + i + "]//div[contains(@class,'ant-select ant-select-enabled')]";
            // 普通输入框
            inputBox = "(" + childDiv + ")[" + i + "]//input[contains(@placeholder,'请输入') and not(@disabled)]";
            // 日期输入框
            dateBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-calendar-picker-input ant-input']";
            // 数量输入框
            numBox = "(" + childDiv + ")[" + i + "]//input[@class='ant-input-number-input']";

            // 若常规输入框在此块级元素中存在
            if (isElementContained(inputBox)) {
                String text = driver.findElement(By.xpath(inputBox)).getAttribute("value");
                log.info("输入框中的值："+text);
                /**
                 * 1.1 首先判断对应框中是否有值。没有值才做后面的校验
                 * */
                if (text== null || "".equals(text)) {
                    /**
                     * 1.2 判断是否有提示语的框
                     * */
                    if (!isElementContained(childrenDiv + "//div[@class='ant-form-explain']")) {
                        return false;
                    } else {
                        /**
                         * 1.3判断有没有对应的提示，不能出现英文提示
                         * 当没有对应提示，且提示中有英文，就false
                         * */
                        String explainText=driver.findElement(By.xpath(childrenDiv + "//div[@class='ant-form-explain']")).getText();
                        if (explainText.indexOf("请输入") == -1 && StringTool.isENChar(explainText)) {
                            return false;
                        }
                    }
                }
            }

            // 下拉框在此块级元素中存在
            else if (isElementContained(selectBox)) {
                //下拉框中数据框的xpath
                String text_xpath = selectBox + "//div[@class='ant-select-selection-selected-value']";
                /**
                 * 2.1 首先判断对应框中是否有值。没有值才做后面的校验
                 * */
                if (isElementContained(text_xpath)) {
                    //判断数据框是否有值，有值则不应该有提示，就跳过这次循环，进行下一次循环
                    if (!"".equals(driver.findElement(By.xpath(text_xpath)))) {
                        continue;
                    }
                } else {
                    /**
                     * 2.2 判断有无红色实线的框 根据 tabindex属性的值，为0就为红色实线，为1就没有红色
                     * */
                    // 定位xpath应该为selectBox下一层的div
                    if (!"0".equals(driver.findElement(By.xpath(selectBox + "//div")).getAttribute("tabindex"))) {
                        return false;
                    } else {
                        //即为块级childrenDiv 下面有无class属性为ant-form-explain的div，不用selectBox的xpath，防止前端代码不严谨问题
                        if (!isElementContained(childrenDiv + "//div[@class='ant-form-explain']")) {
                            return false;
                        } else {
                            /**
                             * 2.3判断有没有对应的提示，不能出现英文提示
                             * 当没有对应提示，且提示中有英文，就false
                             * */
                            String explainText=driver.findElement(By.xpath(childrenDiv + "//div[@class='ant-form-explain']")).getText();
                            if (explainText.indexOf("请选择") == -1 && StringTool.isENChar(explainText)) {
                                return false;
                            }
                        }
                    }

                }
            }

            //若禁止选择下拉框在此块级元素中存在
            else if (isElementContained(selectDisabledBox)) {
                //text_xpath为禁止下拉框中包含值的那一层div，首先判断有无该div，再判断该div中值是否不为空
                String text_xpath = selectDisabledBox + "//div[@class='ant-select-selection-selected-value']";
                if (!isElementContained(text_xpath)) {
                    return false;
                } else {
                    if ("".equals(driver.findElement(By.xpath(text_xpath)).getText())) {
                        return false;
                    }
                }
            }

            // 若日期输入框在此块级元素中存在
            else if (isElementContained(dateBox)) {
                String text = driver.findElement(By.xpath(dateBox)).getAttribute("value");
                log.info("输入框中的值："+ text);
                /**
                 * 1.1 首先判断对应框中是否有值。没有值才做后面的校验
                 * */
                if (text== null || "".equals(text)) {
                    /**
                     * 1.2 判断是否有提示语的框
                     * */
                    if (!isElementContained(childrenDiv + "//div[@class='ant-form-explain']")) {
                        return false;
                    } else {
                        /**
                         * 1.3判断有没有对应的提示，不能出现英文提示
                         * 当没有对应提示，且提示中有英文，就false
                         * */
                        String explainText=driver.findElement(By.xpath(childrenDiv + "//div[@class='ant-form-explain']")).getText();
                        if (explainText.indexOf("请输入") == -1 && StringTool.isENChar(explainText)) {
                            return false;
                        }
                    }
                }

            }
            // 若数量输入框在此块级元素中存在
            else if (isElementContained(numBox)) {

            }
        }
        return true;
    }


    /**
     * 判断搜索框中数据是否被清空，返回布尔类型
     *
     * @param boxType 新建或者搜索框的类型，是滑动框 slide，还是模态框 modal，还是一个页面显示 tab
     * @return 返回各个框中数据是否被清空的布尔类型
     * @throws Exception 当输入的 boxType 类型有问题时候抛出
     */
    protected boolean isCleared(String boxType) throws Exception {
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
                    String controls = driver.findElement(By.xpath(selectBox + "/div[1]")).getAttribute("aria-controls");
                    String text = driver.findElement(By.xpath(selectBox + "//div[@class='ant-select-selection-selected-value']")).getText();
                    clickButton(0, By.xpath(selectBox), 500);
                    String li1Box = driver.findElement(By.xpath("//div[@id='" + controls + "']//ul/li[1]")).getText();
                    if (!text.equals(li1Box)) {
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


    /**
     * 判断搜索框中数据是否被清空，返回布尔类型
     *
     * @param boxType           新建或者搜索框的类型，是滑动框 slide，还是模态框 modal，还是一个页面显示 tab
     * @param defaultAccountSet 默认的账套
     * @return 返回各个框中数据是否被清空的布尔类型
     * @throws Exception 当输入的 boxType 类型有问题时候抛出
     */
    protected boolean isCleared(String boxType, String defaultAccountSet) throws Exception {
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
                    String controls = driver.findElement(By.xpath(selectBox + "/div[1]")).getAttribute("aria-controls");
                    String text = driver.findElement(By.xpath(selectBox + "//div[@class='ant-select-selection-selected-value']")).getText();
                    if (!text.equals(defaultAccountSet)) {
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


    /**
     * 传入产生模态框的按钮定位或者不传都可以
     * 模态框中数据进行输入操作，搜索操作，勾选搜索到数据的操作，最后点击模态框最下面的确定按钮
     *
     * @param data 输入数据数组
     * @param by   （非必输）点击产生模态框的按钮的 By 定位
     * @throws Exception inputAllData 中抛出
     */
    protected void inputModal(String data[], By... by) throws Exception {
        Thread.sleep(500);
        // 产生模态框的按钮 By 定位
        if (by.length != 0) {
            clickButton(by[0]);
        }
        /*点击模态框中的更多图标*/
        if(isElementContained("(//div[@class='ant-modal-body']//i[@class='anticon anticon-down'])[last()]")){
            clickButton(100,By.xpath("(//div[@class='ant-modal-body']//i[@class='anticon anticon-down'])[last()]"),100);
        }
        // 模态框中输入数据
        inputAllDataByData("modal", data);
        // 点击搜索按钮
        log.info("在Modal模态框点击搜索按钮");
        clickButton(1000, By.xpath("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']/div[@class='ant-modal-body']//form//button[@class='ant-btn ant-btn-primary']"), 0);
        // 最多等待 40s 等到暂无数据消失
        for (int i = 0; i < 400; i++) {
            if (!isElementContained("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']//div[@class='ant-empty-image']")) {
                break;
            }
            // 若等到了 10s 再次点击一下搜索按钮
            if (i == 100) {
                log.info("若等到了 10s 再次点击一下搜索按钮");
                clickButton(By.xpath("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']/div[@class='ant-modal-body']//form//button[@class='ant-btn ant-btn-primary']"));
            }
            // 若等到第 20s 再次点击一下搜索按钮
            if (i == 200) {
                log.info("若等到第 20s 再次点击一下搜索按钮");
                Actions actions = new Actions(driver);
                actions.sendKeys(Keys.ENTER).perform();
            }
            // 若等到第 30s 再次点击一下搜索按钮
            if (i == 300) {
                log.info("若等到第 30s 再次点击一下搜索按钮");
                Actions actions = new Actions(driver);
                actions.click(driver.findElement(By.xpath("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']/div[@class='ant-modal-body']//form//button[@class='ant-btn ant-btn-primary']"))).perform();
            }
            Thread.sleep(100);
        }
        // table 中点击第一行第一列
        log.info(" table 中点击第一行第一列");
        clickButton(By.xpath("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']//table/tbody/tr[1]/td[1]"));
        // 点击 footer 中的蓝色确定按钮
        clickButton(0, By.xpath("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']/div[@class='ant-modal-footer']//button[@class='ant-btn ant-btn-primary']"), 500);
    }

    /**
     * 页码选择操作
     *
     * @param value 参数一（非必传）：boxType 可传可不传，tab，slide，modal，传的话定位更准确一些
     *              参数二：rowsPerPage 多少条数据/每页，为 "" 表示默认当前
     *              参数三：page 第几页，此处可以为 ""，表示 input 框不去输入
     * @throws Exception 跳转第几页的 input 不允许为空
     */
    protected void choosePageNum(String... value) throws Exception {
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
        String selectBox = "("+parentDiv + "//ul[@class='ant-pagination ant-table-pagination mini']/li[last()]/div[1]/div)[last()]";
        scrollElementBottomToBottom(By.xpath(selectBox));
        // 定位到下拉框是哪一个 li
        String liBox;
        // 定位到跳转到哪一页的 input 框
        String inputBox = parentDiv + "//ul[@class='ant-pagination ant-table-pagination mini']/li[last()]/div[2]/input";
        // 只有当 rowsPerPage 不为 "" 或者不为 null 时候才去选择每页多少条数据，否则使用默认
        if (rowsPerPage != null && !rowsPerPage.equals("")) {
            liBox = parentDiv + "//ul[@class='ant-pagination ant-table-pagination mini']/li[last()]/div//ul/li[text()='" + rowsPerPage + "']";
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
     * 传入 tab 或者 modal 或者 slide，一般都是传入 tab，然后返回该页面列表中可以显示的行数
     *
     * @param boxType boxType 新建或者搜索框的类型，是滑动框 slide，还是模态框 modal，还是一个页面显示 tab
     * @return 返回该页面中列表的行数 int 类型
     * @throws Exception 你输入的弹框类型有误或者暂不支持！
     */
    protected int rowsOfTable(String boxType) throws Exception {
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
            parentDiv = "//div/div/div[not(@style='display: none;')][2]//div[@class='ant-modal-content']";
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
        tableRowsBox = "("+parentDiv + "//div[@class='ant-table-wrapper'])[last()]//table/tbody/tr";
        tableRowsNum = driver.findElements(By.xpath(tableRowsBox)).size();
        return tableRowsNum;
    }

    /**
     * 传当前框类型，被检测数据数组，需要匹配的列数组，返回是否匹配成功的布尔值
     *
     * @param boxType tab，modal 或者 slide
     * @param data    需要和 table 列表中第几列作比较的 String 数组数据
     * @param td      需要和 data[] 数组中数据作比较的列数 int 数组。PS：data["001", "张三", "男"]，td[1, 3, 6]，即表示检测"001"
     *                是否在第 1 列中被检测到，并且"张三"是否在第 3 列中被检测到，并且"男"是否在第 6 列被检测到，返回布尔类型
     * @return 返回 data[] 数据是否在 td[] 这些列中被检测到的布尔值
     * @throws Exception 你输入的弹框类型有误或者暂不支持
     */
    protected boolean isDataMatchTable(String boxType, String[] data, int[] td) throws Exception {
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
            parentDiv = "//div/div/div[not(@style='display: none;')][2]//div[@class='ant-modal-content']";
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
        scrollElementTopToTop(By.xpath("("+parentDiv + "//div[@class='ant-table-wrapper'])[last()]//table/tbody"));
        // 循环列表中所有的行
        for (int i = 1; i <= rowsOfTable(boxType); i++) {
            // 若 flag 不真直接跳出
            if (!flag) {
                break;
            }
            // 当前在哪一行
            tableRowBox = "("+parentDiv + "//div[@class='ant-table-wrapper'])[last()]//table/tbody/tr[" + i + "]";
            // 循环 td[] 中指定的列
            for (int j = 0; j < td.length; j++) {
                // 若 flag 不真直接跳出
                if (!flag) {
                    break;
                }
                // 当前在哪一列
                tableColumnBox = tableRowBox + "/td[" + td[j] + "]";
                // 若该行该列值不匹配，则 flag 置为 false
                if (!driver.findElement(By.xpath(tableColumnBox)).getText().contains(data[j])) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * 传入"success"或者"error"，判断 Toast 弹框类型是否符合要求的布尔值
     *
     * @param toastStatus Toast 弹框状态，可填"success"或者"error"或者"warn"
     * @return 返回弹框类型是否符合的布尔值
     * @throws Exception 目前暂不支持的 Toast 状态！
     */
    protected boolean isToastCorrect(String toastStatus) throws Exception {
        boolean flag;
        // 弹框 xpath
        String toastBox = "(//div[@class='ant-message-notice-content']/div[contains(@class,'success') or contains(@class,'error') or contains(@class,'warn')])[last()]";
        // 自定义等到弹框出现，最大等待 30 s
        for (int i = 0; i < 30; i++) {
            // 如果 Toast 此时出现
            if (isElementContained(toastBox)) {
                break;
            }
            Thread.sleep(1000);
        }
        // 如果是 success
        if (toastStatus.equals("success")) {
            flag = driver.findElement(By.xpath(toastBox)).getAttribute("class").contains("success");
            for (int i = 0; i < 50; i++) {
                Thread.sleep(100);
                if (!isElementContained("//div[@class='ant-message-notice-content']/div")) {
                    break;
                }
            }
            return flag;
        } else if (toastStatus.equals("error")) {
            flag = driver.findElement(By.xpath(toastBox)).getAttribute("class").contains("error");
            for (int i = 0; i < 50; i++) {
                Thread.sleep(100);
                if (!isElementContained("//div[@class='ant-message-notice-content']/div")) {
                    break;
                }
            }
            return flag;
        } else if (toastStatus.equals("warn")) {
            flag = driver.findElement(By.xpath(toastBox)).getAttribute("class").contains("warn");
            for (int i = 0; i < 50; i++) {
                Thread.sleep(100);
                if (!isElementContained("//div[@class='ant-message-notice-content']/div")) {
                    break;
                }
            }
            return flag;
        } else {
            throw new Exception("目前暂不支持的 Toast 状态！");
        }
    }

    /**
     * 传入"success"或者"error"，判断 Toast 弹框类型是否符合要求的布尔值，text参数判断toast框中文本模糊匹配
     *
     * @param toastStatus Toast 弹框状态，可填"success"或者"error"
     * @param text        toast框中文本 模糊匹配
     * @return 返回弹框类型是否符合的布尔值
     * @throws Exception 目前暂不支持的 Toast 状态！
     */
    protected boolean isQuickToastCorrect(String toastStatus, String text) throws Exception {
        boolean flag1, flag2;
        // 弹框 xpath
        String toastBox = "(//div[@class='ant-message-notice-content']/div[contains(@class,'success') or contains(@class,'error') or contains(@class,'warn')])[last()]";
        // 等到弹框可见
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(toastBox)));
        // 如果是 success
        if (toastStatus.equals("success")) {
            flag1 = driver.findElement(By.xpath(toastBox)).getAttribute("class").contains("success");
            flag2 = driver.findElement(By.xpath(toastBox + "/span")).getText().contains(text);
            for (int i = 0; i < 50; i++) {
                Thread.sleep(100);
                if (!isElementContained("//div[@class='ant-message-notice-content']/div")) {
                    break;
                }
            }
            return flag1 && flag2;
        } else if (toastStatus.equals("error")) {
            flag1 = driver.findElement(By.xpath(toastBox)).getAttribute("class").contains("error");
            flag2 = driver.findElement(By.xpath(toastBox + "/span")).getText().contains(text);
            for (int i = 0; i < 50; i++) {
                Thread.sleep(100);
                if (!isElementContained("//div[@class='ant-message-notice-content']/div")) {
                    break;
                }
            }
            return flag1 && flag2;
        }else if(toastStatus.equals("warn")){
            flag1 = driver.findElement(By.xpath(toastBox)).getAttribute("class").contains("warn");
            flag2 = driver.findElement(By.xpath(toastBox + "/span")).getText().contains(text);
            for (int i = 0; i < 50; i++) {
                Thread.sleep(100);
                if (!isElementContained("//div[@class='ant-message-notice-content']/div")) {
                    break;
                }
            }
            return flag1 && flag2;
        }
        else {
            throw new Exception("目前暂不支持的 Toast 状态！");
        }
    }

    /**
     * 用于相关单据新建编辑后的数据校验
     * @param parentDiv 详情页面的定位
     *                  例如报账单中为 (//div[@id='expense_report_detail']//div[@class='ant-spin-container'])[1]
     *                  项目申请单中为 (//div[@class='pre-payment-detail']//div[@class='ant-spin-container'])[1]
     *                  费用预提单中为 (//div[@class='custom-card']//div[@class='ant-spin-container'])[1]
     * @param newData 用于校验的数据   为二维数组
     *                一般来说 头信息大致数据占一行，备注占一行 文件占一行
     *                如果新建时没有填写备注和附件信息，那么这个二维数据应该只有一行
     *                具体可以在页面中用定位看一下即可明白
     *
     * @return  返回校验的结果
     * @throws Exception 找不到元素异常
     */
    public boolean checkData(String parentDiv,String[][] newData)throws Exception{
        log.info("新建或者编辑显示数据规范检查");
        String childrenDiv=parentDiv+"//div[@class='ant-row']";
        String iChildrenDiv;
        log.info("二维数组维数"+newData.length);
        for(int i=0;i<newData.length;i++){
            log.info("当前维数"+i+"的长度为"+newData.length);
            for(int j=0;j<newData[i].length;j++){
                String iChildrenDiv1="("+childrenDiv+")["+(i+2)+"]/div["+(j+1)+"]//span";
                String iChildrenDiv2="("+childrenDiv+")["+(i+2)+"]/div["+(j+1)+"]//a";
                if(isElementContained(iChildrenDiv1)){
                    iChildrenDiv=iChildrenDiv1;
                }else if(isElementContained(iChildrenDiv2)){
                    iChildrenDiv = iChildrenDiv2;
                }else{
                    throw new Exception("出现特殊的定位");
                }
                if(!newData[i][j].equals(driver.findElement(By.xpath(iChildrenDiv)).getText())){
                    log.info("出错的数据为下标i为"+i+"下标j为"+j);
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 上传文件，传入被点击按钮中的 input 和文件名（无需给 input 改状态）
     *
     * @param inputBy  上传文件当中 input 定位
     * @param fileName 上传文件名，实际上是上传的文件相对于 download 文件夹的相对路径，比如xxx/xxx.png
     */
    synchronized protected void uploadFiles(By inputBy, String fileName) throws Exception {
        // 若是 Windows 系统允许上传文件操作
        if (System.getProperty("os.name").contains("Windows")) {
            // 点击之后保证页面上所有的转圈加载是加载完的
            for (int i = 0; i < 300; i++) {
                if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                    break;
                }
                Thread.sleep(100);
            }
            // 更改 inputBy 的元素为 block
            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
            WebElement webElement = driver.findElement(inputBy);
            javascriptExecutor.executeScript("arguments[0].setAttribute('style','display:block;');", webElement);
            // 上传文件（绝对路径）
            String projectPath = this.getClass().getClassLoader().getResource("./").getPath();
            String absolutePath = new File(projectPath + "../../src/test/resources/download/" + fileName).getCanonicalPath();
            webElement.sendKeys(absolutePath);
            // 回车
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ENTER).build().perform();
            // 还原 inputBy 的样式
            WebElement webElement2 = driver.findElement(inputBy);
            javascriptExecutor.executeScript("arguments[0].setAttribute('style','display:none;');", webElement2);
            // 点击之后保证页面上所有的转圈加载是加载完的
            for (int i = 0; i < 300; i++) {
                if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                    break;
                }
                Thread.sleep(100);
            }
        } else if (System.getProperty("os.name").contains("Linux")) {
            String filePath = null;
            // 点击之后保证页面上所有的转圈加载是加载完的
            for (int i = 0; i < 300; i++) {
                if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                    break;
                }
                Thread.sleep(100);
            }
            // 更改 inputBy 的元素为 block
            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
            WebElement webElement = driver.findElement(inputBy);
            javascriptExecutor.executeScript("arguments[0].setAttribute('style','display:block;');", webElement);
            // 上传文件（绝对路径）
//            log.info("当前工程路径" + System.getProperty("user.dir"));//user.dir指定了当前工程的路径
//            log.info("当前工程路径" + new File("").getAbsolutePath()); //当前工程目录
//            File file = new File(System.getProperty("user.dir") + "/src/test/resources/download/" + fileName);//指定文件目录，工程目录（project+/src/main）
//            File file = new File(System.getProperty("user.dir") + "/opt/autotest/download/" + fileName);//指定文件目录，
           filePath = "/opt/autotest/download/"+ fileName;
            log.info("文件docker路径是" + filePath);
//            log.info("文件路径是否存在" + file.exists());
//            log.info("文件绝对路径" + file.getAbsolutePath());
//            String absolutePath = file.getAbsolutePath();
            webElement.sendKeys(filePath);
            // 回车
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ENTER).build().perform();
            // 还原 inputBy 的样式
            WebElement webElement2 = driver.findElement(inputBy);
            javascriptExecutor.executeScript("arguments[0].setAttribute('style','display:none;');", webElement2);
            // 点击之后保证页面上所有的转圈加载是加载完的
            for (int i = 0; i < 300; i++) {
                if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                    break;
                }
                Thread.sleep(100);
            }
        }
        log.info("上传成功");
    }

    /**
     * 导入文件，先点击导入按钮，下载模板后，往模板里头写数据，然后点击右下角导入，根据文件名成功导入
     *
     * @param by       第一个导入按钮定位
     * @param fileName 需要如导入的文件名相对于 download 的路径（可以是模板文件或者非模板文件）， 如 xxx.png 或者 xxx/xxx.png
     * @param data     写入 fileName 文件的数据
     */
    synchronized protected void importFiles(By by, String fileName, List<String[]> data) throws Exception {
        // 若是 Windows 系统允许上传文件操作
        if (System.getProperty("os.name").contains("Windows")) {
            /* 1.点击第一个导入按钮 */
            clickButton(by);
            /* 2.先删除文件（线程安全） */
            FileTool.deleteFile(new File("").getCanonicalPath() + "/src/test/resources/download/" + fileName);
            /* 3.点击模板下载 */
            long originSize = FileTool.getFileSize(new File(new File("").getCanonicalPath() + "/src/test/resources/download/" + fileName));
            clickButton(By.xpath("//div[@class='ant-modal-content' and not(@style='display: none;')]//button[@class='ant-btn ant-btn-primary ant-btn-sm']"));
            /* 4.确保文件刚好下载完 */
            for (int i = 0; i < 30; i++) {
                // 先获取一下 download 文件夹整体大小
                long sizeBefore = FileTool.getFileSize(new File(new File("").getCanonicalPath() + "/src/test/resources/download/" + fileName));
                Thread.sleep(1000);
                // 经过 1s 后再获取一下 download 这个文件夹的整体大小
                long sizeAfter = FileTool.getFileSize(new File(new File("").getCanonicalPath() + "/src/test/resources/download/" + fileName));
                // 若经过 1s 后文件夹存量没有增加就判断为下载完成
                if (sizeBefore == sizeAfter && sizeBefore != originSize) {
                    break;
                }
            }
            /* 5.对文件进行写操作（线程安全） */
            ExcelTool.writeExcel(fileName, data);
            /* 6.对右下角导入中的 input 进行输入操作，输入需要导入的文件名 */
            uploadFiles(By.xpath("//div[@class='ant-modal-content' and not(@style='display: none;')]//div[@class='ant-modal-content']//button[@class='ant-btn ant-btn-primary ant-btn-sm']"), fileName);
        }
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
                    inputData(By.xpath(tab_input_xpath), "".equals(data[i-1])?ClockTool.getTime():data[i-1]);
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

}