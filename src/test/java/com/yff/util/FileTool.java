package com.yff.util;

import java.io.File;

/**
 * 文件操作的工具类
 *
 * @author YFF
 * @version 1.0.0
 * @date 2019/10/30
 */
public class FileTool {
    /**
     * 私有化工具类，拒绝工具类实例化
     */
    private FileTool() {
        throw new Error("FileTool 不允许实例化！");
    }

    /**
     * 删除文件操作（非强制删除）
     *
     * @param filePath 文件绝对路径
     */
    synchronized public static boolean deleteFile(String filePath){
        // 创建一个文件类
        File file = new File(filePath);
        // 若文件存在
        return file.delete();
    }

    /**
     * 获取文件或者文件夹大小
     *
     * @param file 需要被识别的文件或者文件夹
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        // 若 file 是文件夹【file 是递归中的第 1 个元素】
        if(file.isDirectory()){
            // 拿到其中 file 数组（其中既有文件夹又有文件）
            File[] fileArray = file.listFiles();
            // 深度遍历
            for(int i = 0; i < fileArray.length; i++){
                // 若是文件夹【fileArray[i] 是递归中的第 2 个元素】
                if(fileArray[i].isDirectory()){
                    // 【递归中的递归方式】
                    size = size + getFileSize(fileArray[i]);
                }
                // 若是文件
                else{
                    size = size + fileArray[i].length();
                }
            }
        }
        // 否则 file 是文件
        else {
            // 【递归出口】
            size = size + file.length();
        }
        return size;
    }
}
