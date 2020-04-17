package com.yff.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author YFF
 * @date 2020/4/4
 * 参考链接：
 *      1.IReport接口的使用
 *          https://www.cnblogs.com/zhangfei/p/4514697.html
 *          https://blog.51cto.com/357712148/2367141
 */
@Slf4j
public class TestReportListener implements IReporter{
    // 日期格式化
    private static Date date = new Date();
    private static SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyyMMdd,HH点mm分ss秒");
    private static String reportdate = simpleDateFormat .format(date);
    private static String getReportName = "HCF_WebUI_TestReport" + reportdate;

    // 定义html模板所在路径
    private String templatePath = this.getClass().getResource("/").getPath() + "report/template.html";
    // 定义报告生成的路径
    private String reportDirPath = System.getProperty("user.dir") + File.separator +"target" + File.separator + "test-output" + File.separator + "report";
    private String reportPath = reportDirPath  + File.separator + getReportName + ".html";

    private int testsPass;
    private int testsFail;
    private int testsSkip;
    private String beginTime;
    private long totalTime;
    private String project = "融智汇WebUI自动化测试报告";

    /**
     * 获取各个xml中的执行结果并调用其他方法将结果写入到报告中
     * @param xmlSuites  各个xmlSuite
     * @param suites    各个suites结果
     * @param outputDirectory 输出目录
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        List<ITestResult> list = new ArrayList<>();
        for (ISuite suite : suites) {
            /*这里可以理解为每个suite对应一个xml，其中testng也算一个。*/
            log.info("suite的名称"+suite.getName());

            /*获取每个suite的Results，也就是每个suite中的功能点，testng获取的就是一个空的*/
            Map<String, ISuiteResult> suiteResults = suite.getResults();
            log.info("suiteResults的长度："+suiteResults.size());

            for(String s:suiteResults.keySet()){
                log.info("测试功能名称："+s);
                ISuiteResult  suiteResult=suiteResults.get(s);
                log.info(suiteResult.toString());
                /*获取该测试功能点的测试结果*/
                ITestContext testContext = suiteResult.getTestContext();

                /*获取结果中用例成功的*/
                IResultMap passedTests = testContext.getPassedTests();
                testsPass = testsPass + passedTests.size();

                /*获取结果中用例失败的*/
                IResultMap failedTests = testContext.getFailedTests();
                testsFail = testsFail + failedTests.size();

                /*获取结果中用例跳过的*/
                IResultMap skippedTests = testContext.getSkippedTests();
                testsSkip = testsSkip + skippedTests.size();

                /*获取结果中用例失败的相关信息*/
                IResultMap failedConfig = testContext.getFailedConfigurations();

                list.addAll(this.listTestResult(passedTests));
                list.addAll(this.listTestResult(failedTests));
                list.addAll(this.listTestResult(skippedTests));
                list.addAll(this.listTestResult(failedConfig));
            }
        }
        log.info("list集合的长度"+list.size());
        this.sort(list);
        this.outputResult(list);
    }

    /**
     * 将读出来的list结果输入到报告中
     * @param list 结果的list集合
     */
    private void outputResult(List<ITestResult> list) {
        try {
            /*定义listInfo集合，存储每个用例的执行结果*/
            List<ReportInfo> listInfo = new ArrayList<>();

            /*定义起始的编号*/
            int index = 0;

            /*循环list，每个result对象对应一个测试用例*/
            for (ITestResult result : list) {
                String testName = result.getTestContext().getCurrentXmlTest().getName();
                log.info("testName的值"+testName);
                if(index==0){
                    SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmssSSS");
                    beginTime = formatter.format(new Date(result.getStartMillis()));
                    index++;
                }
                /*计算测试用例所用的时间，并将测试用例时间合计得到总时间*/
                long spendTime = result.getEndMillis() - result.getStartMillis();
                totalTime += spendTime;
                /*获取测试用例的执行结果状态*/
                String status = this.getStatus(result.getStatus());

                /*获取测试用例的日志信息*/
                List<String> log = Reporter.getOutput(result);
                for (int i = 0; i < log.size(); i++) {
                    log.set(i, log.get(i).replaceAll("\"", "\\\\\""));
                }
                Throwable throwable = result.getThrowable();
                if(throwable!=null){
                    log.add(throwable.toString().replaceAll("\"", "\\\\\""));
                    StackTraceElement[] st = throwable.getStackTrace();
                    for (StackTraceElement stackTraceElement : st) {
                        log.add(("    " + stackTraceElement).replaceAll("\"", "\\\\\""));
                    }
                }
                /*定义ReportInfo对应，存储一个测试用例的信息*/
                ReportInfo info = new ReportInfo();
                info.setName(testName);
                info.setSpendTime(spendTime+"ms");
                info.setStatus(status);
                info.setClassName(result.getInstanceName());
                info.setMethodName(result.getName());
                info.setDescription(result.getMethod().getDescription());
                info.setLog(log);

                /*将每个测试用例的测试结果信息存储在listInfo中*/
                listInfo.add(info);
            }

            /*此处对应测试结果的大致数据，对应测试报告中上部分的汇总报告*/
            Map<String, Object> result = new HashMap<>();
            log.info("总运行时间为："+totalTime);
            result.put("testName", this.project);
            result.put("testPass", testsPass);
            result.put("testFail", testsFail);
            result.put("testSkip", testsSkip);
            result.put("testAll", testsPass+testsFail+testsSkip);
            result.put("beginTime", beginTime);
            result.put("totalTime", totalTime+"ms");

            /*将上面每个测试用例的测试结果信息的listInfo存储在result的Map中*/
            result.put("testResult", listInfo);

            /*定义gson对象*/
            Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
            /*获取报告模板中的文本内容*/
            String template = this.read(reportDirPath, templatePath);
            /*定义报告文件的文件流*/
            BufferedWriter output = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(new File(reportPath)),"UTF-8"));
            /*用监听器得到的数据去填写模板内容*/
            template = template.replace("${resultData}", gson.toJson(result));
            /*将模板文件写入到报告中*/
            output.write(template);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建报告生成目录并读取报告模板中的文本并返回报告模板中的字符串
     * @param reportDirPath 报告生成目录
     * @param templatePath 报告模板文件路径
     * @return  返回字符串
     */
    private String read(String reportDirPath, String templatePath) {
        //文件夹不存在时级联创建目录
        File reportDir = new File(reportDirPath);
        if (!reportDir.exists() && !reportDir.isDirectory()) {
            reportDir.mkdirs();
        }
        File templateFile = new File( templatePath );
        InputStream inputStream = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            inputStream = new FileInputStream(templateFile);
            int index = 0;
            byte[] b = new byte[1024];
            while ((index = inputStream.read(b)) != -1) {
                stringBuffer.append(new String(b, 0, index));
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 用于对测试类中的每个类中得到测试结果转换为List集合
     * @param resultMap 测试类测试结果映射的集合
     * @return 返回结果的List集合
     */
    private ArrayList<ITestResult> listTestResult(IResultMap resultMap) {
        Set<ITestResult> results = resultMap.getAllResults();
        return new ArrayList<ITestResult>(results);
    }

    /**
     * 定义的静态内部类，类的属性对应生成报告里面的显示字段。  显示字段分别为 ：
     *  name（用例名称）  className（测试类）  method（测试方法） description（描述）
     *  spendTime（花费时间） status（结果状态）    log（日志）
     */
    public static class ReportInfo {

        private String name;

        private String className;

        private String methodName;

        private String description;

        private String spendTime;

        private String status;

        private List<String> log;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getSpendTime() {
            return spendTime;
        }

        public void setSpendTime(String spendTime) {
            this.spendTime = spendTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<String> getLog() {
            return log;
        }

        public void setLog(List<String> log) {
            this.log = log;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    /**
     * 获取运行的总时间
     * @return  返回totalTime的值
     */
    public long getTime(){
        return totalTime;
    }

    /**
     * 获取执行结果状态对应的字符串
     * @param status 结果状态数字       1、2、3
     * @return 结果状态的字符串         成功、失败、跳过
     */
    private String getStatus(int status) {
        String statusString = null;
        switch (status) {
            case 1:
                statusString = "成功";
                break;
            case 2:
                statusString = "失败";
                break;
            case 3:
                statusString = "跳过";
                break;
            default:
                break;
        }
        return statusString;
    }

    /**
     * 排序方法 对list进行排序
     * @param list 排序的list列表
     */
    private void sort(List<ITestResult> list) {
        Collections.sort(list, new Comparator<ITestResult>() {
            @Override
            public int compare(ITestResult r1, ITestResult r2) {
                return r1.getStartMillis() < r2.getStartMillis() ? -1 : 1;
            }
        });
    }

}

