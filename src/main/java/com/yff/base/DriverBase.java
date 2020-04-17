package com.yff.base;

import com.yff.utils.MyRemoteWebDriver;
import com.yff.utils.PropertyReader;
import com.yff.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @author YFF
 * @date 2020/4/4
 * 参考链接：
 * 1.ChromeOptions类--谷歌浏览器参数设置
 *      https://blog.csdn.net/zwq912318834/article/details/78933910
 *
 * 2.DesiredCapabilities类--selenium gird分布式设置参数决定脚本在哪个环境运行
 *      https://article.itxueyuan.com/4kWxxB
 *
 * 3.RemoteWebDriver类 用于执行机与发送命令机器分离
 *      https://www.cnblogs.com/hejing-swust/articles/8052969.html
 */
@Slf4j
public class DriverBase {

    private WebDriver driver;
    private String browseName;
    private String driverBasePath;

    /**
     * 定义的获取properties文件中各个driver值的变量
     * */
    private String chromeDriverLinuxPath;
    private String chromeDriverPath;
    private String firefoxDriverPath;
    private String ieDriverPath;
    private String edgeDriverPath;

    /**
     * 创建两个ThreadLocal类，分别为Driver的和BrowseName
     * */
    private static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();
    private static ThreadLocal<String> threadBrowseName = new ThreadLocal<>();

    /**
     * 创建两个ThreadLocalUtil类，分别用于操作上面创建的两个ThreadLocal对象
     * */
    private static ThreadLocalUtil<WebDriver> driverThreadLocalUtil = new ThreadLocalUtil<>();
    private static ThreadLocalUtil<String> browseNameThreadLocalUtil = new ThreadLocalUtil<>();

    /**
     * 读取properties文件，赋值
     * */
    private void setValue(){
        this.chromeDriverLinuxPath = PropertyReader.getProperty("driver.chromeDriverLinux");
        this.chromeDriverPath = PropertyReader.getProperty("driver.chromeDriver");
        this.firefoxDriverPath = PropertyReader.getProperty("driver.firefoxDriver");
        this.ieDriverPath = PropertyReader.getProperty("driver.ieDriver");
        this.edgeDriverPath = PropertyReader.getProperty("driver.edgeDriver");
    }

    /**
     * 随机启动浏览器
     * */
    public void randomOpenBrowse(int browserNumber, String remoteIP, String browserVersion){
        /*先获取properties文件中各浏览器driver值*/
        setValue();

        /*获取到target中test-classess目录路径*/
        driverBasePath = this.getClass().getResource("/").getPath();
        log.info("\n driverBasePath的值为："+driverBasePath);

        /*根据参数值是否为1-5，若不为该范围内的值，则随机1-5的值*/
        ArrayList<Integer> number = new ArrayList<>( Arrays.asList(1,2,3,4,5));
        if (!number.contains( browserNumber )){
            Random random = new Random();
            browserNumber = random.nextInt(4)+1;
        }

        /*根据参数启动不同的浏览器*/
        switch (browserNumber){
            case 1:
                openChromeBrowse(remoteIP,browserVersion);
                break;
            case 2:
                openFirefoxBrowse(remoteIP,browserVersion);
                break;
            case 3:
                openEdgeBrowse(remoteIP,browserVersion);
                break;
            case 4:
                openIEBrowse(remoteIP,browserVersion);
                break;
            case 5:
                openChromeMobilePhone(remoteIP,browserVersion);
                break;
            default:
                log.info("参数不符合期望");
                break;
        }
    }

    /**
     * 启动谷歌浏览器
     * */
    private void openChromeBrowse(String remoteIP, String browserVersion){
        try {
            // 如果没有匹配到remoteIP，就执行本机
            if(remoteIP == null || "".equals(remoteIP)) {
                /*如果是 Windows 系统*/
                if(System.getProperty("os.name").contains("Windows")) {
                    /**
                     * 1.设置谷歌浏览器Driver
                     * */
                    System.setProperty("webdriver.chrome.driver", driverBasePath + chromeDriverPath);

                    /**
                     * 2.谷歌浏览器 添加实验性质的设置参数
                     *  设置默认下载路径 且设置为禁止弹出下载窗口
                     * */
                    String downloadPath = new File("").getCanonicalPath()+"\\src\\test\\resources\\download";
                    //创建一个map集合，key为字符串，value为Object类型
                    HashMap<String, Object> chromePrefs = new HashMap<>();
                    chromePrefs.put("profile.default_content_settings.popups", 0);
                    chromePrefs.put("download.default_directory", downloadPath);
                    //创建ChromeOptions对象，并使用方法设置属性
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setExperimentalOption("prefs",chromePrefs);

                    /**
                     * 3.设置启动参数
                     * */
                    /*取消沙盒模式*/
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    /*启动就最大化*/
                    chromeOptions.addArguments("--start-maximized");
                    /*设置静默模式，后台运行*/
                    /*chromeOptions.addArguments("headless");*/

                    /**
                     * 4.设置ThreadLocal对象的值
                     *   threadDriver为创建的ChromeDriver浏览器driver，并加载chromeOptions参数
                     *   threadBrowseName为谷歌
                     * */
                    driverThreadLocalUtil.setThreadValue(threadDriver, new ChromeDriver(chromeOptions));
                    browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                    log.info("成功启动谷歌浏览器");
                }
                /*如果是 Linux 系统*/
                else if(System.getProperty("os.name").contains("Linux")){
                    /*配置和本地Windows类似，且一般都是本地Windows，所以这里一般很难调用到*/
                    System.setProperty("webdriver.chrome.driver", driverBasePath + chromeDriverLinuxPath);
                    // 新的下载地址
                    String downloadPath = new File("").getCanonicalPath() + "\\src\\test\\resources\\download";
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("download.default_directory", downloadPath);
                    // ChromeOptions 中设置下载路径信息，需要传入保存有下载路径的 map
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setExperimentalOption("prefs", hashMap);

                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    driverThreadLocalUtil.setThreadValue(threadDriver, new ChromeDriver(chromeOptions));
                    browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                    log.info("成功启动谷歌浏览器");
                }
            }
            else{
                /**
                 *1.创建DesiredCapabilities 配置谷歌浏览器、版本号、平台
                 * */
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities("chrome", browserVersion, Platform.LINUX);

                /**
                 * 2. 创建ChromeOptions对象，并使用merge方法添加desiredCapabilities参数
                 * */
                ChromeOptions chromeOptions = new ChromeOptions().merge(desiredCapabilities);

                /**
                 * 3.谷歌浏览器 添加实验性质的设置参数 设置默认下载路径
                 * */
                String downloadPath = new File("").getCanonicalPath() + "\\src\\test\\resources\\download";
                // map 中保存下载地址信息
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("download.default_directory", downloadPath);
                chromeOptions.setExperimentalOption("prefs", hashMap);

                /**
                 * 4.设置启动参数
                 * */
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");

                /**
                 * 5.创建driver对象
                 * RemoteWebDriver(URL remoteAddress, Capabilities capabilities)
                 * 第一个参数代表服务器地址、第二个参数表示预期的执行对象
                 * */
                /**/
                this.driver = new RemoteWebDriver(new URL("http://" + remoteIP + ":9002/wd/hub/"), chromeOptions);

                /**
                 * 6.设置ThreadLocal对象的值
                 *   threadDriver为创建的ChromeDriver浏览器driver
                 *   threadBrowseName为谷歌
                 * */
                driverThreadLocalUtil.setThreadValue(threadDriver, driver);
                browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                log.info("成功启动谷歌浏览器");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("谷歌浏览器启动失败");
        }
    }

    /**
     * 启动火狐浏览器
     * */
    private void openFirefoxBrowse(String remoteIP, String browserVersion){
        try {
            System.setProperty("webdriver.gecko.driver", driverBasePath + firefoxDriverPath);
            driverThreadLocalUtil.setThreadValue( threadDriver, new FirefoxDriver() );
            browseNameThreadLocalUtil.setThreadValue( threadBrowseName, "火狐" );
            log.info( "成功启动火狐浏览器" );
        }catch (Exception e){
            e.printStackTrace();
            log.info("火狐浏览器启动失败");
        }
    }

    /**
     * 启动Edge浏览器
     * */
    private void openEdgeBrowse(String remoteIP, String browserVersion){
        try {
            System.setProperty("webdriver.edge.driver", driverBasePath + edgeDriverPath);
            driverThreadLocalUtil.setThreadValue( threadDriver, new EdgeDriver() );
            browseNameThreadLocalUtil.setThreadValue( threadBrowseName, "Edge" );
            log.info( "成功启动Edge浏览器" );
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("Edge浏览器启动失败");
        }
    }

    /**
     * 启动IE浏览器
     * */
    private void openIEBrowse(String remoteIP, String browserVersion){
        try {
            System.setProperty("webdriver.ie.driver", driverBasePath + ieDriverPath);
            // ie浏览器安全设置
            DesiredCapabilities ieCapabilities  = DesiredCapabilities.internetExplorer ();
            ieCapabilities.setCapability ( InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true );
            InternetExplorerOptions option = new InternetExplorerOptions ( ieCapabilities );

            driverThreadLocalUtil.setThreadValue( threadDriver, new InternetExplorerDriver(option) );
            browseNameThreadLocalUtil.setThreadValue( threadBrowseName, "IE" );
            log.info( "成功启动IE浏览器" );
        }catch (Exception e){
            e.printStackTrace();
            log.info("IE浏览器启动失败");
        }
    }

    /**
     * 启动谷歌手机模拟浏览器
     * */
    private void openChromeMobilePhone(String remoteIP, String browserVersion){
        try {
            if(remoteIP == null || remoteIP.equals("")) {
                // 若是 windows 系统
                if(System.getProperty("os.name").contains("Windows")) {
                    System.setProperty("webdriver.chrome.driver", driverBasePath + chromeDriverPath);
                    Map<String, String> mobileEmulation = new HashMap<>();
                    mobileEmulation.put("deviceName", "Nexus 5");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    driverThreadLocalUtil.setThreadValue(threadDriver, new ChromeDriver(chromeOptions));
                    browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                    log.info("成功启动谷歌手机模拟浏览器");
                }
                // 如果是 Linux 系统
                else if(System.getProperty("os.name").contains("Linux")){
//                            System.setProperty(BaseConstant.CHROME_DRIVER_NAME, driverBasePath + chromeDriverLinuxPath);
                    Map<String, String> mobileEmulation = new HashMap<>();
                    mobileEmulation.put("deviceName", "Nexus 5");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    driverThreadLocalUtil.setThreadValue(threadDriver, new ChromeDriver(chromeOptions));
                    browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                    log.info("成功启动谷歌手机模拟浏览器");
                }
            }
            else{
                // 设置属性
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", "Nexus 5");

                DesiredCapabilities desiredCapabilities = new DesiredCapabilities("chrome", browserVersion, Platform.LINUX);
                ChromeOptions chromeOptions = new ChromeOptions().merge(desiredCapabilities);

//                        ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                // 驱动使用 RemoteWebDriver
                this.driver = new MyRemoteWebDriver(new URL("http://" + remoteIP + ":4444/wd/hub/"), chromeOptions);
                driverThreadLocalUtil.setThreadValue(threadDriver, driver);
                browseNameThreadLocalUtil.setThreadValue(threadBrowseName, "谷歌");
                log.info("成功启动谷歌手机模拟浏览器");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("谷歌手机模拟浏览器启动失败");
        }
    }

   /**
    * 获得driver
    * */
    public WebDriver getDriver(){
        return driverThreadLocalUtil.getThreadValue( threadDriver );
    }

    /**
     * 设置driver
     * */
    private void setDriver(WebDriver driver){
        this.driver = driver;
    }

    /**
     * 关闭driver
     * */
    public void stopDriver(){
        setDriver( getDriver() );
        setBrowseName( getBrowseName());
        if(driver != null){
            driver.quit();
            log.info("成功关闭"  + browseName + "浏览器");
            /*最后通过remove方法去掉对应的线程组*/
            threadDriver.remove();
            threadBrowseName.remove();
        }
    }

    /**
     * 获得BrowseName
     * */
    private String getBrowseName(){
        return browseNameThreadLocalUtil.getThreadValue( threadBrowseName );
    }

    /**
     * 设置BrowseName
     * */
    private void setBrowseName(String browseName){
        this.browseName = browseName;
    }


    public static void main(String[] args) {
        log.info(System.getProperty("os.name"));
    }

}
