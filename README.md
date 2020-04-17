# Java Selenium UI自动化框架

<a name="YVkPx"></a>
## 一、GitHub链接
项目网址链接：<br />[https://github.com/yff9802/Java_Selenium](https://github.com/yff9802/Java_Selenium)<br />代码克隆链接：<br />git@github.com:yff9802/Java_Selenium.git        SSH方式<br />[https://github.com/yff9802/Java_Selenium.git](https://github.com/yff9802/Java_Selenium.git)    HTTPS方式
<a name="jMCff"></a>
## 二、框架结构图
框架用到Java+Maven+Selenium+Tesng+Jedis+Jenkins持续集成，除去Jenkins持续集成，其他相关知识点和知识点的参考链接都在框架代码中有写到，可以克隆代码后学习。由于是该框架在工作中用到，在框架中实际的案例中，将系统登录的方法注释掉并将相关信息删除，所以实际是运行不了。但是整体框架的知识点都已体现出来，只是把具体的业务代码删除和登录的网址、账号、密码、服务器IP都注释了。<br />![系统架构图1.png](https://cdn.nlark.com/yuque/0/2020/png/701726/1587145123762-f81ef683-3792-41c7-814b-f68f0742ecc7.png#align=left&display=inline&height=341&margin=%5Bobject%20Object%5D&name=%E7%B3%BB%E7%BB%9F%E6%9E%B6%E6%9E%84%E5%9B%BE1.png&originHeight=454&originWidth=670&size=27384&status=done&style=stroke&width=503)

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
<a name="YoWRl"></a>
## 三、框架运行注意点及运行大体逻辑
<a name="umxeb"></a>
### 3.1 运行注意点

1. 由于框架中用到Jedis，所以在本地运行Testng.xml文件的时候，需要打开Redis。
1. 运行的时候一般都是运行的对应的xml文件。
<a name="1lrHA"></a>
### 3.2运行大体逻辑
下面是一个大体的运行逻辑，然后就是监听器监听执行的@Test的方法，并输出日志、错误时截图、运行完成后生成报告等。
![](https://cdn.nlark.com/yuque/__puml/40a24b0a06ad801edffc6cb96a59d64b.svg#lake_card_v2=eyJjb2RlIjoiQHN0YXJ0bWluZG1hcFxuKyAxLui_kOihjFhNTOaWh-S7ti0-XFxuMi7ov5DooYzlhbbkuK3nmoRUZXN057G7XG4rKyBAQmVmb3JlVGVzdFxuKysrIOiwg-eUqERyaXZlckJhc2XvvIzlsIZkcml2ZXLlr7nosaHlrZjmlL7lnKhkcml2ZXLmsaDkuK1cbisrKyDosIPnlKhKZWRpc0Jhc2XvvIzlsIZqZWRpc-WvueixoeWtmOaUvuWcqEplZGlzUG9vbOS4rVxuKysgQEJlZm9yZUNsYXNzXG4rKysg5LuOZHJpdmVy5rGg5Lit6I635Y-WZHJpdmVy5a-56LGhXG4rKysg5Yib5bu66ZqQ5byP562J5b6F55qE5a-56LGhd2FpdFxuKysrIOS7jkplZGlzUG9vbOS4reiOt-WPlkplZGlz5a-56LGhXG4rKyBAQmVmb3JlQ2xhc3NcbisrKyDns7vnu5_nmbvlvZVcbisrIFRlc3TkuK3mr4_kuKrmlrnms5VcbisrKyBUZXN05Lit6LCD55SoUGFnZeWxguaWueazlVxuKysrIFBhZ2XlsYLkvJrosIPnlKjlhbHnlKjnmoRQYWdlQ29tbW9u5Lit5pa55rOVXFxu5bm25Lya55So5YiwTG9jYXRvcuWxgueahOWumuS9jeWSjERhdGHlsYLnmoTmlbDmja5cbisrIEBBZnRlclRlc3RcbisrKyDlhbPpl61Ecml2ZXLlr7nosaFcbisrKyDlvZLov5hKZWRpc-WvueixoVxuKysrIOW9kui_mFdlYkRyaXZlcldhaXTlr7nosaFcbkBlbmRtaW5kbWFwIiwidHlwZSI6InB1bWwiLCJtYXJnaW4iOnRydWUsImlkIjoiVkZiMmYiLCJ1cmwiOiJodHRwczovL2Nkbi5ubGFyay5jb20veXVxdWUvX19wdW1sLzQwYTI0YjBhMDZhZDgwMWVkZmZjNmNiOTZhNTlkNjRiLnN2ZyIsImhlaWdodCI6NDgwLCJjYXJkIjoiZGlhZ3JhbSJ9)<a name="vCV1K"></a>
### 3.3运行结果报告
[报告.html](https://www.yuque.com/attachments/yuque/0/2020/html/701726/1587147127717-cea0dbcd-4e72-4e53-b294-de6caacbb2a0.html?_lake_card=%7B%22uid%22%3A%221587147103012-0%22%2C%22src%22%3A%22https%3A%2F%2Fwww.yuque.com%2Fattachments%2Fyuque%2F0%2F2020%2Fhtml%2F701726%2F1587147127717-cea0dbcd-4e72-4e53-b294-de6caacbb2a0.html%22%2C%22name%22%3A%22%E6%8A%A5%E5%91%8A.html%22%2C%22size%22%3A1019151%2C%22type%22%3A%22text%2Fhtml%22%2C%22ext%22%3A%22html%22%2C%22progress%22%3A%7B%22percent%22%3A99%7D%2C%22status%22%3A%22done%22%2C%22percent%22%3A0%2C%22id%22%3A%22YwH5w%22%2C%22card%22%3A%22file%22%7D)
