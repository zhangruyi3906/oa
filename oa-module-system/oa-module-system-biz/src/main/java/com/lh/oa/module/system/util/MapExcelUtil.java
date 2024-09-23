package com.lh.oa.module.system.util;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapExcelUtil {


    private static MapExcelUtil instance = new MapExcelUtil();

    private MapExcelUtil() {
    }

    public static MapExcelUtil getInstance() {
        return instance;
    }

    /**
     * 将 List<Map<String,Object>> 类型的数据导出为 Excel
     * 默认 Excel 文件的输出路径为 项目根目录下
     * 文件名为 filename + 时间戳 + .xlsx
     *
     * @param mapList  数据源(通常为数据库查询数据)
     * @param filename 文件名前缀, 实际文件名后会加上日期
     * @param title    表格首行标题
     * @return 文件输出路径
     */
    public static void createExcel(List<Map<String, Object>> mapList, String filename, String title, HttpServletResponse response) {
        //获取数据源的 key, 用于获取列数及设置标题
        Map<String, Object> map = mapList.get(0);
        Set<String> stringSet = map.keySet();
        ArrayList<String> headList = new ArrayList<>(stringSet);
        //定义一个新的工作簿
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建一个Sheet页
        XSSFSheet sheet = wb.createSheet(title);
        //设置行高
        sheet.setDefaultRowHeight((short) (2 * 256));
        //为有数据的每列设置列宽
        for (int i = 0; i < headList.size(); i++) {
            sheet.setColumnWidth(i, 2000);
        }
        //设置单元格字体样式
        XSSFFont font = wb.createFont();
        font.setFontName("等线");
        font.setFontHeightInPoints((short) 16);
        //在sheet里创建第一行，并设置单元格内容为 title (标题)
        XSSFRow titleRow = sheet.createRow(0);
        XSSFCell titleCell = titleRow.createCell(0);
        title = title + "(" + mapList.get(0).get(headList.get(0)) + ")";
        titleCell.setCellValue(title);
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headList.size() - 1));
        // 创建单元格文字居中样式并设置标题单元格居中
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        titleCell.setCellStyle(cellStyle);

        //获得表格第二行
        XSSFRow row = sheet.createRow(1);
        //根据数据源信息给第二行每一列设置标题
        for (int i = 0; i < headList.size(); i++) {
            XSSFCell cell = row.createCell(i);
            String head = headList.get(i);
            if (head.equals("name")) {
                head = "名称";
            }
            if (head.contains("day")) {
                head = head.replace("day", "");
            }
            cell.setCellValue(head);
        }
        XSSFRow rows;
        XSSFCell cells;
        //循环拿到的数据给所有行每一列设置对应的值
        for (int i = 0; i < mapList.size(); i++) {
            //在这个sheet页里创建一行
            rows = sheet.createRow(i + 2);
            //给该行数据赋值
            for (int j = 0; j < headList.size(); j++) {
                String value;
                Object data = mapList.get(i).get(headList.get(j));
                if (data != null) {
                    value = data.toString();
                    if (data instanceof Integer) {
                        if (data.equals(1)) {
                            value = "√";
                        } else if (data.equals(0)) {
                            value = "×";
                        }
                    }
                } else {
                    value = "";
                }
                if (i == 0 && j == 0) {
                    value = "当日未写总数";
                }
                cells = rows.createCell(j);
                cells.setCellValue(value);
            }
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            filename = URLEncoder.encode(filename, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(filename));
            OutputStream out = response.getOutputStream();
            wb.write(out);
            wb.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
