package com.lh.oa.framework.excel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.lh.oa.framework.common.util.json.JsonUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 实现EXCEL自定义下拉项的拦截器
 * 下拉方面，该拦截器只能实现简单的单选下拉，树形下拉和复选框暂时没有找到实现方法，需要调研
 * 同时可以实现多输入框级联下拉
 *
 * @author tanghanlin
 * @since 2023/11/27
 */
public class DropListWriteHandler implements SheetWriteHandler {

    /**
     * 列数和自定义下拉项列表的映射关系
     */
    private Map<Integer, List<String>> colAndDropNameListMap;

    /**
     * 多输入框父子关系级联下拉的映射关系
     */
    private Map<Integer, Map<String, List<String>>> colAndParentDropListMap;

    public DropListWriteHandler() {
    }

    public DropListWriteHandler(Map<Integer, List<String>> colAndDropNameListMap, Map<Integer, Map<String, List<String>>> colAndParentDropListMap) {
        this.colAndDropNameListMap = colAndDropNameListMap;
        this.colAndParentDropListMap = colAndParentDropListMap;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 获取第一个sheet页
        Sheet sheet = writeSheetHolder.getCachedSheet();
        // 获取sheet页的数据校验对象
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 获取工作簿对象，用于创建存放下拉数据的字典sheet数据页
        Workbook workbook = writeWorkbookHolder.getWorkbook();

        int index = 1;
         // 下拉设置的最大行数，默认设置1000个，主要是级联设置的话需要循环，很影响性能
        int maxRowNum = 500;
        if (!CollectionUtils.isEmpty(colAndDropNameListMap)) {
            for (Map.Entry<Integer, List<String>> entry : colAndDropNameListMap.entrySet()) {
                Integer colIndex = entry.getKey();
                List<String> dropNameList = entry.getValue();
                // 50个下拉项以内，可以直接实现
                if (dropNameList.size() <= 50) {
                    // 区间设置：指定需要设置下拉项的列，目前只设置第一行
                    CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, maxRowNum, colIndex, colIndex);
                    DataValidationConstraint constraint = helper.createExplicitListConstraint(dropNameList.toArray(new String[0]));
                    DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
                    setValidation(sheet, helper, constraint, cellRangeAddressList);
                    sheet.addValidationData(dataValidation);
                }
                // 50个以上，直接设置会被从50截断，需要单独做一个sheet关联来绕过这个问题
                else {
                    // 普通数据，迭代索引，用于存放下拉数据的字典sheet数据页命名
                    // 设置存放下拉数据的字典sheet，并把这些sheet隐藏掉，这样用户交互更友好
                    String dictSheetName = "dict_hide_sheet" + index;
                    Sheet dictSheet = workbook.createSheet(dictSheetName);
                    // 隐藏字典sheet页
                    workbook.setSheetHidden(index, true);

                    // 设置下拉列表覆盖的行数，目前就只指定第一行的示例数据
                    // 如果要设置到最后一行，行索引是1048575，千万别写成1048576，不然会导致下拉列表失效，出不来
                    CellRangeAddressList infoList = new CellRangeAddressList(1, maxRowNum, entry.getKey(), entry.getKey());
                    int rowLen = entry.getValue().size();
                    for (int i = 0; i < rowLen; i++) {
                        // 向字典sheet写数据，从第一行开始写，此处可根据自己业务需要，自定
                        // 义从第几行还是写，写的时候注意一下行索引是从0开始的即可
                        dictSheet.createRow(i).createCell(0).setCellValue(entry.getValue().get(i));
                    }

                    // 设置关联数据公式，这个格式跟Excel设置有效性数据的表达式是一样的
                    String refers = dictSheetName + "!$A$1:$A$" + entry.getValue().size();
                    Name name = workbook.createName();
                    name.setNameName(dictSheetName);
                    // 将关联公式和sheet页做关联
                    name.setRefersToFormula(refers);

                    // 将上面设置好的下拉列表字典sheet页和目标sheet关联起来
                    DataValidationConstraint constraint = helper.createFormulaListConstraint(dictSheetName);
                    setValidation(sheet, helper, constraint, infoList);
                    index++;
                }
            }
        }

        if (!CollectionUtils.isEmpty(colAndParentDropListMap)) {
            for (Map.Entry<Integer, Map<String, List<String>>> entry : colAndParentDropListMap.entrySet()) {
                Integer colIndex = entry.getKey();
                Map<String, List<String>> parentChildDropList = entry.getValue();
                List<String> parentDropList = new ArrayList<>(parentChildDropList.keySet());

                // 填充父子类的sheet，用来做对应的下拉
                Sheet parentSheet = workbook.createSheet("site" + index);
                workbook.setSheetHidden(index, true);
                // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
                int rowId = 0;
                Row parentSheetRow = parentSheet.createRow(rowId++);
                parentSheetRow.createCell(0).setCellValue("大类列表");
                for (int i = 0; i < parentDropList.size(); i++) {
                    Cell cell = parentSheetRow.createCell(i + 1);
                    cell.setCellValue(parentDropList.get(i));
                }

                // 子类sheet
                for (Map.Entry<String, List<String>> parentChildEntry : parentChildDropList.entrySet()) {
                    String key = parentChildEntry.getKey();
                    List<String> childDropList = parentChildEntry.getValue();

                    Row row = parentSheet.createRow(rowId++);
                    row.createCell(0).setCellValue(key);
                    for (int i = 0; i < childDropList.size(); i++) {
                        Cell cell = row.createCell(i + 1);
                        cell.setCellValue(childDropList.get(i));
                    }
                    // 添加名称管理器
                    String range = getRange(1, rowId, childDropList.size());
                    Name name = workbook.createName();
                    name.setNameName(key);
                    String formula = "site" + index + "!" + range;
                    name.setRefersToFormula(formula);
                }

                // 设置（父类子类）下拉框
                DataValidationHelper dvHelper = sheet.getDataValidationHelper();
                // 父类规则
                DataValidationConstraint expConstraint = dvHelper.createExplicitListConstraint(parentDropList.toArray(new String[0]));
                CellRangeAddressList expRangeAddressList = new CellRangeAddressList(1, maxRowNum, colIndex, colIndex);
                setValidation(sheet, dvHelper, expConstraint, expRangeAddressList);

                // 子类规则(各单元格按个设置)
                // "INDIRECT($A$" + 2 + ")" 表示规则数据会从名称管理器中获取key与单元格 A2 值相同的数据，如果A2是浙江省，那么此处就是浙江省下面的市
                // 为了让每个单元格的公式能动态适应，使用循环挨个给公式。
                // 循环几次，就有几个单元格生效，次数要和上面的大类影响行数一一对应，要不然最后几个没对上的单元格实现不了级联
                // 这里的循环从2开始，是因为级联数据的sheet页，是从A2开始的
                for (int i = 2; i < maxRowNum + 2; i++) {
                    CellRangeAddressList rangeAddressList = new CellRangeAddressList(i - 1, i - 1, colIndex + 1, colIndex + 1);
                    DataValidationConstraint formula = dvHelper.createFormulaListConstraint("INDIRECT($D$" + i + ")");
                    setValidation(sheet, dvHelper, formula, rangeAddressList);
                    index++;
                }
            }
        }
    }

    /**
     * 设置验证规则
     *
     * @param sheet       sheet对象
     * @param helper      验证助手
     * @param constraint  createExplicitListConstraint
     * @param addressList 验证位置对象
     */
    private void setValidation(Sheet sheet, DataValidationHelper helper, DataValidationConstraint constraint, CellRangeAddressList addressList) {
        DataValidation dataValidation = helper.createValidation(constraint, addressList);
        sheet.addValidationData(dataValidation);
    }

    /**
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     * @author denggonghai 2016年8月31日 下午5:17:49
     */
    public String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }

}