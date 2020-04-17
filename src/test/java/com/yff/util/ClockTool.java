package com.yff.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Random;

/**
 * 时间（精确到 ms）工具类
 *
 * @author YFF
 * @version 1.0.0
 * @date 2019/8/22
 */
@Slf4j
public class ClockTool {
    /**
     * 私有化工具类，拒绝工具类实例化
     */
    private ClockTool() {
        throw new Error("ClockTool 不允许实例化！");
    }

    /**
     * 获取 String 拼接的当前时间
     */
    public static String getCurrentTime() {
        // 获取当前时间，精确到毫秒级别
        Calendar cld = Calendar.getInstance();
        int YY = cld.get(Calendar.YEAR);
        int MM = cld.get(Calendar.MONTH) + 1;
        int DD = cld.get(Calendar.DATE);
        int HH = cld.get(Calendar.HOUR_OF_DAY);
        int mm = cld.get(Calendar.MINUTE);
        int SS = cld.get(Calendar.SECOND);
        int MI = cld.get(Calendar.MILLISECOND);
        // 返回当前时间，精确到毫秒
        return "" + MM + DD + HH + mm + SS + MI;
    }

    /**
     * 获取String拼接的当前时间年月日
     */
    public static String getTime() {
        // 获取当前时间，精确到毫秒级别
        Calendar cld = Calendar.getInstance();
        String YY = cld.get(Calendar.YEAR) + "";
        String MM = (cld.get(Calendar.MONTH) + 1 + "").length() > 1 ? (cld.get(Calendar.MONTH) + 1 + "") : "0" + (cld.get(Calendar.MONTH) + 1 + "");
        String DD = (cld.get(Calendar.DATE) + "").length() > 1 ? cld.get(Calendar.DATE) + "" : "0" + cld.get(Calendar.DATE);

        return "" + YY + "-" + MM + "-" + DD;
    }


    /**
     * 获取不重复数字 用于我的票夹当中 第一种方法(有错误的概率)
     * @param length 获取的长度 8位或12位
     * @return 返回数字字符串
     */
    public static String getLengthTime(int length) {
        String currentTime = System.currentTimeMillis() + "";
        return currentTime.substring(currentTime.length() - length, currentTime.length());
    }

    /**
     * 获取不重复数字 用于我的票夹当中 第二种方法(重复的概率小)
     * @param length 获取的长度 8位或12位
     * @return 返回数字字符串
     * @throws Exception 参数异常
     */
    public static String getDiffLengthTime(int length) throws Exception {
        String resultString="";

        Calendar cld = Calendar.getInstance();
        String MM = (cld.get(Calendar.MONTH) + 1 + "").length() > 1 ? (cld.get(Calendar.MONTH) + 1 + "") : "0" + (cld.get(Calendar.MONTH) + 1 + "");
        String DD = (cld.get(Calendar.DATE) + "").length() > 1 ? cld.get(Calendar.DATE) + "" : "0" + cld.get(Calendar.DATE);

        if(8==length){
            resultString=MM+DD+getRandomString()+getRandomString()+getRandomString()+getRandomString();
        }else if(12==length){
            resultString=MM+DD+getRandomString()+getRandomString()+getRandomString()+getRandomString()+getRandomString()+getRandomString()+getRandomString()+getRandomString();
        }else {
            log.info("参数只能为8或者12");
            throw new Exception("参数不符合规范");
        }
       return resultString;
    }

    private static String getRandomString(){
        Random random=new Random();
        return random.nextInt(10)+"";
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getDiffLengthTime(12));
    }
}
