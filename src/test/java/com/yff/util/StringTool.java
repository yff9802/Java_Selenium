package com.yff.util;

import java.util.regex.Pattern;

/**
 * 相关字符串的工具类
 * @author YFF
 * @version 1.0.0
 * @date 2020/03/11
 **/
public class StringTool {
    /**
     * 私有化工具类，拒绝工具类实例化
     */
    private StringTool() {
        throw new Error("StringTool 不允许实例化！");
    }

    /**
     * 判断字符串中是否含有英文字母,有英文字母则返回true，没有则返回false
     * 主要用途：新建空值校验中，对于提示语句的要求降低，只需要判断提示语句中没有英文的情况
     * @param string 字符串
     * @return 返回判断的结果
     */
    public static boolean isENChar(String string) {
        Pattern p = Pattern.compile("[a-zA-z]");
        if(p.matcher(string).find()) {
            return true;
        }
        return false;
    }
}
