package com.lh.oa.module.bpm.utils;

import com.lh.oa.framework.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
@Slf4j
public class BpmExportUtil {
    private Workbook wb = null;

    private LinkedHashMap<String, List<LinkedHashMap<String, String>>> data;

    public InputStream outputStream() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream in = null;
        try {
            wb.write(os);
            in = new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            os.close();
        }
        return in;
    }

    //创建workBook
    public void createWorkBook() {
        try {
            wb = new XSSFWorkbook();
        } catch (Exception e) {
            log.info("创建excel工作簿失败:{}", e.getMessage());
            throw new BusinessException("创建excel工作簿失败，请联系管理员");
        }
    }

    public void createSheet() {
        Set<Map.Entry<String, List<LinkedHashMap<String, String>>>> entries = data.entrySet();
        entries.forEach(entry -> {
            String sheetName = entry.getKey();
            List<LinkedHashMap<String, String>> value = entry.getValue();
            if (sheetName.contains("/")) {
                sheetName = sheetName.replace("/", "、");
            }
            Sheet sheet = wb.createSheet(sheetName);
            int rowIndex = 0;
            Row row = sheet.createRow(rowIndex);
            if (value.size() == 0) {
                return;
            }
            Map<String, String> firstValue = value.get(0);
            Set<String> title = firstValue.keySet();
            int index = 0;
            for (String string : title) {
                Cell cell = row.createCell(index);
                cell.setCellValue(string);
                cell.setCellStyle(titleStyle());
                index++;
            }
            rowIndex += 1;
            if (value.size() > 0)
                this.createSheetRowData(sheet, rowIndex, value);
            else
                sheet.autoSizeColumn(rowIndex);
        });
    }

    private void createSheetRowData(Sheet sheet, int rowIndex, List<LinkedHashMap<String, String>> value) {
        for (Map<String, String> stringStringMap : value) {
            final int[] r = {0};
            Row row = sheet.createRow(rowIndex);
            Set<Map.Entry<String, String>> entries = stringStringMap.entrySet();
            entries.forEach(info -> {
                Cell cell = row.createCell(r[0]);
                cell.setCellStyle(textStyle());
                cell.setCellValue(info.getValue());
                r[0]++;
            });
            rowIndex += 1;
        }
    }

    private static void addMergeCellBorder(CellRangeAddress cra, Sheet sheet) {
        RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
    }

    //设置title字体
    private Font titleFont() {
        if (wb == null)
            throw new BusinessException("workbook is null ");
        Font titleFont = wb.createFont();
        titleFont.setFontName("黑体");
        titleFont.setFontHeightInPoints((short) 16);//设置字体大小
        return titleFont;
    }

    //设置title样式
    private CellStyle titleStyle() {
        if (wb == null)
            throw new BusinessException("workbook is null ");
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(titleFont());
        titleStyle.setBorderTop(BorderStyle.THIN);
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return titleStyle;
    }

    //设置正文字体
    private Font textFont() {
        if (wb == null)
            throw new BusinessException("workbook is null ");
        Font textFont = wb.createFont();
        textFont.setFontName("黑体");
        textFont.setFontHeightInPoints((short) 14);//设置字体大小
        return textFont;
    }

    //设置正文样式
    private CellStyle textStyle() {
        if (wb == null)
            throw new BusinessException("workbook is null ");
        CellStyle textStyle = wb.createCellStyle();
        textStyle.setFont(textFont());
        textStyle.setBorderTop(BorderStyle.THIN);
        textStyle.setBorderBottom(BorderStyle.THIN);
        textStyle.setBorderLeft(BorderStyle.THIN);
        textStyle.setBorderRight(BorderStyle.THIN);
        textStyle.setAlignment(HorizontalAlignment.LEFT);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return textStyle;
    }

    public void setData(LinkedHashMap<String, List<LinkedHashMap<String, String>>> data) {
        this.data = data;
    }


}
