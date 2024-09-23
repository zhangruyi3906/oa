package com.lh.oa.module.bpm.utils.export;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ExcelUtils {

    public static int exportToExcelForXlsx1(List<ExportEntry> exportProcessInstanceData, HttpServletResponse response) {
        XSSFWorkbook wb = new XSSFWorkbook();
        String fileName = "流程导出.xlsx";
        exportProcessInstanceData.forEach(entry -> {

            List mergeIndex = new ArrayList();
            for (int i = 0; i < entry.getColumns().size(); i++) {
                if (entry.getMergeIndexSet().contains(i)) {
                    continue;
                }
                mergeIndex.add(i);
            }
            if (CollectionUtils.isEmpty(entry.getObjData()) || entry.getObjData().size() == 0) {
                return;
            }
            ExcelUtils.exportToExcelForXlsx(entry.getObjData(), "fileName", entry.getSheetName(), entry.getColumns(), mergeIndex, response, wb);


//            ExcelUtils.exportToExcelForXlsx1(exportProcessInstanceData,response);
        });
        OutputStream os = null;
        try {
            // 创建一个普通输出流
            os = response.getOutputStream();

            // 请求浏览器打开下载窗口
            response.reset();
            response.setCharacterEncoding("UTF-8");

            fileName = new String(fileName.getBytes(), "ISO8859-1");
            // 要保存的文件名
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentType("application/octet-stream");
            wb.write(os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public static int exportToExcelForXlsx(List<List> objData, String fileName, String sheetName, List<String> columns, List mergeIndex, HttpServletResponse response, XSSFWorkbook wb) {
        int maxSize = 0;//表格编辑框数据行数  是合并非表格编辑框数据的步长
        int index = 1;//标记当前数据所在行
        int flag = 0;

        // 创建工作薄
//        XSSFWorkbook wb = new XSSFWorkbook();

        // sheet1
        XSSFSheet sheet1 = wb.createSheet(sheetName);

        //设置样式
        XSSFCellStyle style = wb.createCellStyle();
        //水平对齐
        style.setAlignment(HorizontalAlignment.CENTER);

        //表头
        //冻结表头
        sheet1.createFreezePane(0, 1);
        XSSFRow sheet1row1 = sheet1.createRow((short) 0);
        sheet1row1.setHeight((short) 480);
        //写入表头
        if (columns != null && columns.size() > 0) {
            for (int i = 0; i < columns.size(); i++) {
                String column = columns.get(i);
                //列
                XSSFCell cell = sheet1row1.createCell(i);
                cell.setCellValue(column);
            }
        }

        //数据开始行
        int dataSatrtIndex = 1;
        boolean isMerge = false;
        if (mergeIndex != null && mergeIndex.size() != 0) {
            isMerge = true;
        }

        //写入数据
        if (objData != null && objData.size() > 0) {
            Map<Integer, PoiModel> poiModels = new HashMap<Integer, PoiModel>();

            //循环写入表中数据
            int i = 0;
            for (; i < objData.size(); i++) {

                //数据行
                XSSFRow row = sheet1.createRow((short) (i + dataSatrtIndex));
                //行内循环，既单元格（列）
                List<Object> list = objData.get(i);

                int o1 = ((Integer) list.get(list.size() - 1)) == 0 ? 0 : ((Integer) list.get(list.size() - 1));//长度
                maxSize = (o1 == 0 ? maxSize : o1);

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                int j = 0;
                int size = list.size();
                for (Object o : list) {
                    if (j == size - 1) break;
                    //数据列
                    String content = "";
                    if (o != null) {
                        if (o.toString().contains(".") && isNumeric(o.toString())) {
                            content = decimalFormat.format(Float.valueOf(o.toString()));
                        } else if (o.toString().contains("-") && o.toString().contains(":")) {
                            content = String.valueOf(o).split("\\.")[0];
                        } else {
                            content = String.valueOf(o);
                        }
                    }

                    if (isMerge && mergeIndex.contains(j)) {
                        //如果该列需要合并
                        PoiModel poiModel = poiModels.get(j);
                        if (poiModel == null) {
                            poiModel = new PoiModel();
                            poiModel.setContent(content);
                            poiModel.setRowIndex(i + dataSatrtIndex);
                            poiModel.setCellIndex(j);
                            poiModels.put(j, poiModel);
                        } else {
//                            if(!poiModel.getContent().equals(content)) {
                            if (i + dataSatrtIndex == index) {
                                //如果不同了，则将前面的数据合并写入
                                XSSFRow lastRow = sheet1.getRow(poiModel.getRowIndex());
                                //创建列
                                XSSFCell lastCell = lastRow.createCell(poiModel.getCellIndex());
                                lastCell.setCellValue(poiModel.getContent());
                                //合并单元格
                                if (poiModel.getRowIndex() != index - 1) {
                                    sheet1.addMergedRegion(new CellRangeAddress(poiModel.getRowIndex(), i + dataSatrtIndex - 1, poiModel.getCellIndex(), poiModel.getCellIndex()));
                                }
                                //将新数据存入
                                if (ObjectUtils.isNotEmpty(content)) {
                                    poiModel.setContent(content);
                                }
                                poiModel.setRowIndex(i + dataSatrtIndex);
                                poiModel.setCellIndex(j);
                                poiModels.put(j, poiModel);
                            }
                        }
                        //创建单元格
                        row.createCell(j);
                    } else {//该列不需要合并
                        //数据列
                        XSSFCell cell = row.createCell(j);
                        cell.setCellValue(content);
                    }
                    j++;
                }
                if ((i + dataSatrtIndex) % maxSize == 0) {
                    index += maxSize;
                }
            }

            //将最后一份存入
            if (poiModels != null && poiModels.size() != 0) {
                for (Integer key : poiModels.keySet()) {
                    PoiModel poiModel = poiModels.get(key);
                    XSSFRow lastRow = sheet1.getRow(poiModel.getRowIndex());
                    XSSFCell lastCell = lastRow.getCell(poiModel.getCellIndex());
                    lastCell.setCellValue(poiModel.getContent());
                    //合并单元格
                    if (poiModel.getRowIndex() != i + dataSatrtIndex - 1) {
                        sheet1.addMergedRegion(new CellRangeAddress(poiModel.getRowIndex(), i + dataSatrtIndex - 1, poiModel.getCellIndex(), poiModel.getCellIndex()));
                    }
                }
            }
        } else {
            flag = -1;
        }

        //设置固定列宽，大概规律网上有不少版本自行百度
        //这里大概是143像素
        for (int i = 0; i < columns.size(); i++) {
            sheet1.setColumnWidth(i, 4550);
        }
        return flag;
    }

    /**
     * 判断是不是数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}