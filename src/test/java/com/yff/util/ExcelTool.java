package com.yff.util;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 操作工具类
 *
 * @author YFF
 * @version 1.0.0
 * @date 2019/10/30
 */
public class ExcelTool {
    /**
     * 私有化工具类，拒绝工具类实例化
     */
    private ExcelTool() {
        throw new Error("ExcelTool 不允许实例化！");
    }

    /**
     * 表格中当前 sheet 页多行输入操作
     *
     * @param excelPath Excel 相对于 download 文件夹的相对路径，如 xxx.xls 或者 xxx/xxx.xls
     * @param data 保存有写入表格多行的数据，类型是 List
     */
    synchronized public static void writeExcel(String excelPath, List<String[]> data) throws Exception {
        // excel 绝对路径
        String absoluteExcelPath = new File("").getCanonicalPath() + File.separator+"src"+ File.separator+"test"+ File.separator+"resources"+ File.separator+"download"+ File.separator+ excelPath;
        // 根据路径创建文件类
//        File excelFile = new File(absoluteExcelPath);
        FileInputStream excelFile = new FileInputStream(new File(absoluteExcelPath));
        // 依据文件类创建工作簿类
        Workbook workbook = WorkbookFactory.create(excelFile);
        // 获取当前 sheet 页
        Sheet sheet = workbook.getSheetAt(0);
        /* 开始逐行遍历（getLastRowNum 可以返回 -1） */
        for(int rowNum = sheet.getLastRowNum() + 1, i = 0; i < data.size(); rowNum++, i++) {
            // 获取新的一行
            Row row = sheet.createRow(rowNum);
            // 当前行的数据
            String[] rowData = data.get(i);
            /* 开始逐列遍历（getLastCellNum 可以返回 -1） */
            for(int columnNum = 0, j = 0; j < rowData.length; columnNum++, j++){
                // 往单元格中写数据
                row.createCell(columnNum).setCellValue(rowData[j]);
            }
        }
        // 文件输出流
        FileOutputStream out=new FileOutputStream(absoluteExcelPath);
        // 将 Workbook 数据写入
        workbook.write(out);
        out.flush();
        // 关闭流
        out.close();
    }

    /**
     * Excel 中第一个 sheet 页进行多行多列数据写入操作
     *
     * @param excelName Excel 文件名，相对于 download 文件夹的相对路径，如 xxx.xls 或者 xxx/xxx.xls
     * @return 返回 List<String[]> 类型读取表格的数据
     */
    synchronized public static List<String[]> readExcel(String excelName) throws Exception {
        // 获取 Excel 文件绝对路径
        String absoluteExcelPath = new File("").getCanonicalPath() + "/src/test/resources/" + excelName;
        // 根据 Excel 路径创建 File 文件类
//        File excelFile = new File(absoluteExcelPath);
        FileInputStream excelFile = new FileInputStream(new File(absoluteExcelPath));
        // 根据 File 文件类创建 Workbook 工作簿类
        Workbook workbook = WorkbookFactory.create(excelFile);
        // 根据 Workbook 获取当前 Sheet 页
        Sheet sheet = workbook.getSheetAt(0);
        List<String[]> dataList = new ArrayList<>();
        /* 开始逐行遍历 */
        for(int rowNum = 0, i = 0; i <= sheet.getLastRowNum(); rowNum++, i++) {
            // 获取新的一行
            Row row = sheet.getRow(rowNum);
            // 创建该行对应的 String[] 数组
            String[] rowString = new String[row.getLastCellNum() + 1];
            /* 开始逐列遍历 */
            for(int columnNum = 0, j = 0; j < row.getLastCellNum(); columnNum++, j++){
                // 获取新的一列
                Cell cell = row.getCell(columnNum);
                // 设置 Cell 类型为 String
                cell.setCellType(CellType.STRING);
                // 将单元格内容保存进 String[] 行数组
                rowString[j] = new DataFormatter().formatCellValue(cell);
            }
            // 将各行数组数据保存进 List 集合
            dataList.add(rowString);
        }
        return dataList;
    }
}
