# Java Selenium UI自动化框架
# Java Selenium UI自动化框架

<a name="YVkPx"></a>
## 一、GitHub链接
项目网址链接：<br />[https://github.com/yff9802/Java_Selenium](https://github.com/yff9802/Java_Selenium)<br />代码克隆链接：<br />git@github.com:yff9802/Java_Selenium.git        SSH方式<br />[https://github.com/yff9802/Java_Selenium.git](https://github.com/yff9802/Java_Selenium.git)    HTTPS方式
<a name="jMCff"></a>
## 二、框架结构图

- main
  - java
    - base
      - **DriverBase**：封装启动本地或远程的各种浏览器的类
      - **JedisBase**：封装利用JedisPool获取和归还Jedis对象，操作Redis
    - utils
      - **MyRemoteWebDriver**：用于启动远程WebDriver的类
      - **PropertyReader**：读取Properties的类
      - **ScreenShot**：截图工具类
      - **ThreadLocalUtil**：用于操作_ThreadLocal的工具类_
  - resources
      - **log4j2.xml**：log4j2的配置文件、控制台输出和文件滚动输出
- test
  - java
    - listener
      - **TestLogListener**：Testng监听接口TestListenerAdapter实现类，用于测试方法不同的操作
      - **TestReportListener**：Testng自定义报告IReporter接口实现类，用于自定义结果报告
    - test
      - base
        - **BaseTest**：
        - **TimeBaseOpt**：WebDriverWait，隐式等待相关设置
      - common
        - 封装页面共用方法，用于Page层调用
      - data
        - 数据层：数据常量做数据驱动，动态数据用jedis对象来保存
      - locator
        - 定位层：用于存放对应页面相关元素的xpath定位
      - page
        - 逻辑层：具体每个测试用例的业务逻辑编写
      - test
        - 业务层：利用Test注解及相关配置，封装成对应的测试方法
    - util
      - 相关工具方法，例如获取当前时间（为了实际业务中单据编号不重复）、操作Excel、文件相关操作等
  - resources
    - config
      - **config.properties**：将配置文件，例如浏览器Driver路径、Redis的IP、端口等
    - download
      - 定义的文件下载的默认路径
    - drivers
      - 存放各种浏览器的driver文件
    - report
      - template.html：生产的测试报告的模板文件，会在报告监听器中用到
- **pom.xml**：Maven配置文件
- **testng.xml**：实际运行的文件

![系统架构图1.png](https://cdn.nlark.com/yuque/0/2020/png/701726/1587127324791-695452e5-169b-4904-a207-41542d65f8a7.png#align=left&display=inline&height=455&margin=%5Bobject%20Object%5D&name=%E7%B3%BB%E7%BB%9F%E6%9E%B6%E6%9E%84%E5%9B%BE1.png&originHeight=455&originWidth=481&size=24931&status=done&style=none&width=481)<br />![系统架构图2.png](https://cdn.nlark.com/yuque/0/2020/png/701726/1587127348025-c08d3bf4-7479-4e04-9a65-09ad00c7b5f8.png#align=left&display=inline&height=302&margin=%5Bobject%20Object%5D&name=%E7%B3%BB%E7%BB%9F%E6%9E%B6%E6%9E%84%E5%9B%BE2.png&originHeight=302&originWidth=469&size=14139&status=done&style=none&width=469)<br />![系统架构图3.png](https://cdn.nlark.com/yuque/0/2020/png/701726/1587127368574-c647d642-b374-4771-98f4-91873b04981a.png#align=left&display=inline&height=487&margin=%5Bobject%20Object%5D&name=%E7%B3%BB%E7%BB%9F%E6%9E%B6%E6%9E%84%E5%9B%BE3.png&originHeight=487&originWidth=463&size=20397&status=done&style=none&width=463)
<a name="6LU5x"></a>
## <br />
