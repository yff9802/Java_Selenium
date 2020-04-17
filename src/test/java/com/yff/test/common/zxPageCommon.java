package com.yff.test.common;

import com.yff.util.ClockTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YFF
 * @date 2020/4/4
 */
@Slf4j
public class zxPageCommon extends PageCommon {

    /**
     * 构造器
     *
     * @param driver 子类传入的驱动
     */
    public zxPageCommon(WebDriver driver, String environment, String language){
        super(driver,environment, language);
    }

    /**
     * 如果打开多标签，关闭当前操作的页面的标签，并且刷新整个页面
     * name = 当前操作的标签名字
     */
    public void CloseTag(String name) throws Exception {
        String NameString = "//span[text()='" + name + "']/../i";
        By NameXpath = By.xpath(NameString);
        clickButton(NameXpath);
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='out-content']/div[1]/span[text()='仪表盘']")));
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
            parentDiv = "//div[@class='ant-modal-content']";
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
     * 搜索功能--特定字段搜索
     */
    public void LowSearch(String data, String title,String Number) throws Exception {
        log.info("执行按字段搜索");
        if (isElementContained("//a[text()='更多']")){
            clickButton(0, By.xpath("//a[text()='更多']"),700);
        }
        /* 特定字段的xpath拼接 */
        String title_xpath = "//label[text()='"+title+"']/../../..//input";
        // 在指定栏输入数据
        inputData(By.xpath(title_xpath),data);
        // 点击搜索
        clickButton(By.xpath("(//form[contains(@class,'search-area')]//button)[1]"));
        // 做校验是否搜索成功
        String search_are = "(//table)[2]/tbody/tr[1]/td["+Number+"]/span[2]";
        // 等到搜索出来的元素可见
        String search_content = driver.findElement(By.xpath(search_are)).getText();
        System.out.println(data);
        System.out.println("嘟嘟嘟嘟");
        System.out.println(search_content);
        Assert.assertTrue(data.equals(search_content));
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
    protected void inputAll_Data(String boxType, String data[], CharSequence... keysToSend) throws Exception {
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
            int b = i+10;
            // 下拉框
            selectBox = "(" + childDiv + ")[" + b + "]//div[contains(@class,'ant-select ant-select-enabled') and not(contains(@class,'ant-select-no-arrow'))]";
            // 普通输入框
            inputBox = "(" + childDiv + ")[" + b + "]//input[contains(@placeholder,'请输入') and not(@disabled)]";
            //多行文本输入框
            textareaBox = "(" + childDiv + ")[" + b + "]//textarea[contains(@placeholder,'请输入') and not(@disabled)]";
            // 开关按钮 禁用
            switch1Box = "(" + childDiv + ")[" + b + "]//button[@class='ant-switch']";
            // 开关按钮 启用
            switch2Box = "(" + childDiv + ")[" + b + "]//button[@class='ant-switch ant-switch-checked']";
            // 日期输入框
            dateBox = "(" + childDiv + ")[" + b + "]//input[@class='ant-calendar-picker-input ant-input']";
            // 数量输入框
            numBox = "(" + childDiv + ")[" + b + "]//input[@class='ant-input-number-input' and not(@disabled)]";
            // 日期选择框
            dateBox2 = "" + childDiv + "";
            // 下拉框在此块级元素中存在
            if (isElementContained(selectBox)) {
                // 点击下拉框
                clickButton(0, By.xpath(selectBox), 0);
                // Actions
                Actions actions = new Actions(driver);
                // controls
                String controls = driver.findElement(By.xpath("(" + childDiv + ")[" + b + "]//span[@class='ant-form-item-children']/div/div")).getAttribute("aria-controls");
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
                String tab_input_xpath = "(" + childDiv + ")[" + b + "]//input[@class='ant-calendar-input ']";
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
                Thread.sleep(500);
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
     * 参数: 除了请输入可请选择的框有几个其他种类的框 a
     * 点击清空按钮，遍历输入框目前识别下拉框和输入框
     * 有不为空的就返回False
     * @throws Exception
     */
    protected boolean CLEAR_BEFORE_SEARCH(String boxType, int a) throws Exception {
        /* ========== 多种类型框的 xpath 定位 ========== */
        // 外框 xpath 定位
        String parentDiv = "";
        // 里头块级元素的 xpath 定位
        String childDiv = "";
        // 输入区域块级元素
        String SEARCH_XPATH = "";
        String PLEASE_PLACE_HOLD = "";
        /* ========== 判断弹出框是滑动框还是模态框还是标签框 ========== */
        // 如果匹配到 modal 模态框
        int Flags = 0;
        if (boxType.equals("modal")) {
            log.info("现在匹配到模态框");
            parentDiv = "//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']";
            // childDiv是页码
            childDiv = parentDiv + "//span[text()='清 空']/..";
            SEARCH_XPATH = parentDiv + "//form[contains(@class,'search-area')]//div[contains(@class,'ant-col-8')]";
        }
        // 如果匹配到 tab 标签框
        else if (boxType.equals("tab")) {
            log.info("现在匹配到标签页");
            parentDiv = "//div[@class='ant-tabs-content ant-tabs-content-no-animated ant-tabs-top-content ant-tabs-card-content']";
            // childDiv是页码
            childDiv = parentDiv + "//span[text()='清 空']/..";
            SEARCH_XPATH = parentDiv + "//form[contains(@class,'search-area')]/div/div/div[contains(@class,'ant-col-6')]";
        }
        // 或者什么都没匹配到
        else {
            log.info("你输入的弹框类型有误或者暂不支持！");
            throw new Exception("你输入的弹框类型有误或者暂不支持！");
        }
        // 开始点击下一页
        // 点击清空按钮
        clickButton(By.xpath(childDiv));
        boolean flag = false;
        // 里头块级元素个数
        int divNum = 0;
        divNum = driver.findElements(By.xpath(SEARCH_XPATH)).size();//4
        // 确定"请输入的个数"
        PLEASE_PLACE_HOLD = SEARCH_XPATH + "//input[@placeholder='请输入']";
        int please_input = 0;
        please_input = driver.findElements(By.xpath(PLEASE_PLACE_HOLD)).size(); //2
        // 循环遍历请输入的框
        for(int i = 1; i <= please_input; i++){
            String div_child = "(" + PLEASE_PLACE_HOLD + ")" +"[" + i + "]";
            String input_text = driver.findElement(By.xpath(div_child)).getText();
            if (input_text==null||"".equals(input_text)){
                continue;
            }else {
                return flag;
            }
        }
        // 统计请选择框的框 --- 会匹配到账套隐藏的placehold
        String PLEASE_CHOOSE = parentDiv + "//div[text()='请选择']";
        int please_int = driver.findElements(By.xpath(PLEASE_CHOOSE)).size();  // 2
        System.out.println(please_input);
        System.out.println(please_int);
        System.out.println(a);
        System.out.println(divNum);
        if (please_input + please_int + a == divNum){
            return true;
        }else {
            return flag;
        }
    }

    /**
     * 弹出框的搜索
     */
    protected int MODULE_SEARCH(String data[], By... by) throws Exception {
        Thread.sleep(500);
        // 产生模态框的按钮 By 定位
        if (by.length != 0) {
            clickButton(by[0]);
        }
        // 模态框中输入数据
        inputAllData("modal", data);
        // 点击搜索按钮
        log.info("在Modal模态框点击搜索按钮");
        clickButton(1000, By.xpath("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']/div[@class='ant-modal-body']//form//button[@class='ant-btn ant-btn-primary']"), 0);
        // 最多等待 40s 等到暂无数据消失
        int num = driver.findElements(By.xpath("//div[@class='ant-modal-wrap ' and not(@style='display: none;')]//div[@class='ant-modal-content']//table/tbody/tr")).size();
        return num;
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
        String selectBox = "("+parentDiv + "//ul[@class='ant-pagination ant-table-pagination mini']/li[last()]/div[1]/div)[1]";
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


    /* ============================== 第一页往上翻页得方法 ============================== */
    /*
     * 私有方法，第一页往上翻页得方法
     * @return 返回选择当前第一条时，是否还能向上翻页
     * @String type 页码条在tab,还是modal。slide,必填
     */
    protected boolean pageUp(String type ) throws Exception {
        String page;
        if(type.equals("tab")) {
            //得到全部的页码条xpath字符串
            page = "(//div[@class='out-content']//ul[contains(@class,'ant-pagination')])[last()]";
        }else if (type.equals("modal")){
            page = "(//div[@class='ant-modal-content']//ul[contains(@class,'ant-pagination')])[last()]";
        }else if (type.equals("slide")){
            page = "(//div[contains(@class,'slide-frame animated slideInRight')]//ul[contains(@class,'ant-pagination')])[last()]";
        }else{
            log.info("此类型不存在，你可真是个小天才");
            return false;
        }
        //先翻到当前页码的第一页
        String firstPage=page+"/li[contains(@class,'ant-pagination-item ant-pagination-item')][1]";
        clickButton(0,By.xpath(firstPage),600);
        //等待页面加载
        // 保证页面上所有的转圈加载是加载完的
        for (int i = 0; i < 300; i++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
        //向上翻页的xpath字符串
        String upPage=page+"/li[contains(@class,'ant-pagination-prev')]";
        //检验向上翻页元素是否可点击
        if(driver.findElement(By.xpath(upPage)).getAttribute("aria-disabled").indexOf("true")<0){
            return false;
        }else{
            return true;
        }
    }


    /* ============================== 最后一页往下翻页得方法 ============================== */
    /*
     * 私有方法，最后一页往下翻页得方法
     * @return 返回选择最后一条时，是否还能向下翻页
     * @String type 页码条在tab,还是modal。slide，必填
     */
    protected boolean pageDown(String type) throws Exception {
        String page;
        if(type.equals("tab")) {
            //得到全部的页码条xpath字符串
            page = "(//div[@class='out-content']//ul[contains(@class,'ant-pagination')])[last()]";
        }else if (type.equals("modal")){
            page = "(//div[@class='ant-modal-content']//ul[contains(@class,'ant-pagination')])[last()]";
        }else if (type.equals("slide")){
            page = "(//div[contains(@class,'slide-frame animated slideInRight')]//ul[contains(@class,'ant-pagination')])[last()]";
        }else{
            log.info("此类型不存在，你可真是个小天才");
            return false;
        }
        //先翻到当前页码的最后一页
        String lastPage=page+"/li[contains(@class,'ant-pagination-item ant-pagination-item')][last()]";
        clickButton(0,By.xpath(lastPage),600);
        //等待页面加载
        // 保证页面上所有的转圈加载是加载完的
        for (int i = 0; i < 300; i++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
        //向下翻页的xpath字符串
        String downPage=page+"/li[contains(@class,'ant-pagination-next')]";
        //检验向下翻页元素是否可点击
        if(driver.findElement(By.xpath(downPage)).getAttribute("aria-disabled").indexOf("true")<0){
            return false;
        }else{
            return true;
        }
    }


    protected boolean PAGE_TEST(String type, String page, String two) throws Exception {
        log.info("页码功能测试");
        // 滑动到最底端
        scrollToBottom();
        // 页码选择,发到第二页
        choosePageNum(type, page, two);
        Thread.sleep(600);
        int intt = Integer.valueOf(page.substring(0, 2));
        if(intt != rowsOfTable(type)){
            log.info("该页面页码条数不符");
            return false;
        }
        if(type.equals("tab")) {
            //得到全部的页码条xpath字符串
            page = "(//div[@class='out-content']//ul[contains(@class,'ant-pagination')])[last()]";
        }else if (type.equals("modal")){
            page = "(//div[@class='ant-modal-content']//ul[contains(@class,'ant-pagination')])[last()]";
        }else if (type.equals("slide")){
            page = "(//div[contains(@class,'slide-frame animated slideInRight')]//ul[contains(@class,'ant-pagination')])[last()]";
        }else{
            log.info("此类型不存在，你可真是个小天才");
            return false;
        }
        //点击向上翻页,从第二页往上翻，翻到第一页
        String up=page+"//i[@class='anticon anticon-left']";
        clickButton(0,By.xpath(up),600);
        if(driver.findElement(By.xpath(page+"//li[contains(@class,'active')]")).getAttribute("class").indexOf("1")<0){
            log.info("向上翻页失败");
            return false;
        }
        //翻回第二页
        String pageNum=page+"//input";
        driver.findElement(By.xpath(pageNum)).clear();
        driver.findElement(By.xpath(pageNum)).sendKeys("2");
        driver.findElement(By.xpath(pageNum)).sendKeys(Keys.ENTER);
        //点击向下翻页,第二页向下翻页，翻到第三页
        String down=page+"//i[@class='anticon anticon-right']";
        clickButton(0,By.xpath(down),600);
        if(driver.findElement(By.xpath(page+"//li[contains(@class,'active')]")).getAttribute("class").indexOf("3")<0){
            log.info("向下翻页失败");
            return false;
        }
        //翻到第9999页，9999页不存在
        driver.findElement(By.xpath(pageNum)).clear();
        driver.findElement(By.xpath(pageNum)).sendKeys("99999");
        driver.findElement(By.xpath(pageNum)).sendKeys(Keys.ENTER);
        //等待页面加载
        // 保证页面上所有的转圈加载是加载完的
        for (int i = 0; i < 300; i++) {
            if (!isElementContained("//span[contains(@class,'ant-spin-dot ant-spin-dot-spin')]")) {
                break;
            }
            Thread.sleep(100);
        }
        //校验当前是否选择的最后一页
        String lastPage=page+"//li[contains(@class,'ant-pagination-item')][last()]";
        if(driver.findElement(By.xpath(lastPage)).getAttribute("class").indexOf("active")<0){
            log.info("跳到不存在页码失败");
            return false;
        }
        //第一条向上翻页,最后一条向下翻页
        if(pageUp(type)){
            if(pageDown(type)){
                return true;
            }else {
                log.info("最后一页向下翻页失败");
                return false;
            }
        }else {
            log.info("第一页向上翻页失败");
            return false;
        }
    }

    /**
     * 参数- 出现滑动框的按钮的xpath
     * 滑动框关闭 点x
     * 返回值:
     *         1----打开滑动框和关闭滑动框其中一个步骤出错
     *         0---- 弹窗打开关闭过程正常
     */
    protected int closeSlideByCross(By xpath) throws Exception {
        // 点击按钮
        int Flag = 0;
        WebElement btn=driver.findElement(xpath);
        Actions action =new Actions(driver);
        action.moveToElement(btn).click().perform();
        // 滑动框xpath定位
        Thread.sleep(1000);
        String slide = "//div[contains(@class,'slide-frame animated slideInRight')]";
        // 打开如果不成功下一步点击x就会报错 不需要校验
        // x的xpath定位
        String Cross = "//div[contains(@class,'slide-frame animated slideInRight')]/div[@class='slide-title']/i";
        clickButton(700,By.xpath(Cross),800);
        if (isElementContained(slide)){// 滑动框存在
            Flag += 1;// 框存在 不满足预期情况
        }else {
            Flag += 0;// 框不存在 满足预期情况
        }
        return Flag;
    }

    /**
     * 参数- 出现滑动框的按钮的xpath
     * 滑动框关闭 点取消按钮
     * 返回值:
     *         1----打开滑动框和关闭滑动框其中一个步骤出错
     *         0---- 弹窗打开关闭过程正常
     */
    protected int closeSlideByCancel(By xpath) throws Exception {
        // 点击按钮
        int Flag = 0;
        WebElement btn=driver.findElement(xpath);
        Actions action =new Actions(driver);
        action.moveToElement(btn).click().perform();
        // 滑动框xpath定位
        Thread.sleep(1000);
        String slide = "//div[contains(@class,'slide-frame animated slideInRight')]";
        // 打开如果不成功下一步点击x就会报错 不需要校验
        // 取消按钮的xpath定位
        String Cross = "//div[contains(@class,'slide-frame animated slideInRight')]//form/div[last()]/button[@class='ant-btn']";
        clickButton(700,By.xpath(Cross),800);
        if (isElementContained(slide)){// 滑动框存在
            Flag += 1;// 框存在 不满足预期情况
        }else {
            Flag += 0;// 框不存在 满足预期情况
        }
        return Flag;
    }


    /**
     * 返回特殊字符
     * @return
     * @throws Exception
     */
    protected String SpecialCharacters() throws Exception {
        String Special = "!@#$%^&*()" + "743146159";
        return Special;
    }

    /**
     * 判断字符串是否全是数字
     */
    public boolean isNumeric(String str) {
        //Pattern pattern = Pattern.compile("^-?[0-9]+"); //这个也行
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");//这个也行
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
