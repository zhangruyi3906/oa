package com.lh.oa.framework.excel.core.util;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.lh.oa.framework.common.exception.util.ServiceExceptionUtil;
import com.lh.oa.framework.excel.handler.DropListWriteHandler;
import com.lh.oa.module.system.enums.ErrorCodeConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel 工具类
 *
 * @author
 */
public class ExcelUtils {

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response 响应
     * @param filename 文件名
     * @param sheetName Excel sheet 名
     * @param head Excel head 头
     * @param data 数据列表哦
     * @param <T> 泛型，保证 head 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
                .autoCloseStream(false) // 不要自动关闭，交给 Servlet 自己处理
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 基于 column 长度，自动适配。最大 255 宽度
                .sheet(sheetName).doWrite(data);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }

    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
       return EasyExcel.read(file.getInputStream(), head, null)
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .doReadAllSync();
    }



    public static <T> Map<String, Object> readSalaryList(MultipartFile file, Class<T> head) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        Object o = reader.readCellValue(25, 0);
        if (ObjectUtils.isEmpty(o)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SALARY_IMPORT_MONTH_IS_EMPTY);
        }
        String month = o.toString();
        reader.addHeaderAlias("序号", "orderNumber");
        reader.addHeaderAlias("部门", "deptName");
        reader.addHeaderAlias("姓名", "username");
        reader.addHeaderAlias("手机号", "mobile");
        reader.addHeaderAlias("入职日期", "hireDate");
        reader.addHeaderAlias("应出勤", "attendance");
        reader.addHeaderAlias("实际出勤", "attendanceDays");
        reader.addHeaderAlias("核算工资出勤", "salaryAttendance");
        reader.addHeaderAlias("综合工资总额", "attendanceSalary");
        reader.addHeaderAlias("工龄", "senioritySalary");
        reader.addHeaderAlias("其他加班", "overtimeSalary");
        reader.addHeaderAlias("方量", "quantitySalary");
        reader.addHeaderAlias("绩效金额", "performance");
        reader.addHeaderAlias("年休工资", "annualLeaveSalary");
        reader.addHeaderAlias("病假工资", "sickLeaveSalary");
        reader.addHeaderAlias("绩效奖励", "performanceSalary");
        reader.addHeaderAlias("其他补助", "subsidies");
        reader.addHeaderAlias("应付工资", "shouldSalary");
        reader.addHeaderAlias("迟到/早退/漏签扣款", "attendanceDeduction");
        reader.addHeaderAlias("其他(考核、微博扣款)", "assessmentDeduction");
        reader.addHeaderAlias("社保", "socialSecurity");
        reader.addHeaderAlias("个税", "personalTax");
        reader.addHeaderAlias("借支", "borrowing");
        reader.addHeaderAlias("其他", "otherDeduction");
        reader.addHeaderAlias("实付工资", "realSalary");
        reader.addHeaderAlias("备注", "remark");

        List<T> salaryList = reader.read(2, 3, head);
        Map<String, Object> salaryMap = new HashMap<>();

        salaryMap.put("month", month);
        salaryMap.put(month, salaryList);
        return salaryMap;
    }

    /**
     * 写excel-实现了自定义下拉框功能
     *
     * @param response              响应
     * @param filename              文件名
     * @param sheetName             Excel sheet 名
     * @param head                  Excel head 头
     * @param data                  数据列表
     * @param <T>                   泛型，保证 head 和 data 类型的一致性
     * @param colAndDropNameListMap 列数和自定义下拉项列表的映射关系
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data, Map<Integer, List<String>> colAndDropNameListMap,
                                 Map<Integer, Map<String, List<String>>> colAndParentDropListMap) throws IOException {
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
                .autoCloseStream(false) // 不要自动关闭，交给 Servlet 自己处理
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 基于 column 长度，自动适配。最大 255 宽度
                .registerWriteHandler(new DropListWriteHandler(colAndDropNameListMap, colAndParentDropListMap))
                .sheet(sheetName)
                .doWrite(data);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }

}
