package com.lh.oa.module.system.controller.admin.user.vo.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.lh.oa.framework.excel.core.annotations.DictFormat;
import com.lh.oa.framework.excel.core.convert.DictConvert;
import com.lh.oa.module.system.enums.DictTypeConstants;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户 Excel 导出 VO
 */
@Data
public class UserExcelVO {

    @ExcelProperty("账号")
    private String username;

    @ExcelProperty("姓名")
    private String nickname;

    @ExcelProperty("职级")
    private String level;

    @ExcelProperty("一级部门")
    private String parentDeptName;

    @ExcelProperty("部门")
    private String deptName;

    @ExcelProperty("手机号")
    private String mobile;

    @ExcelProperty("岗位")
    private String postName;

    @ExcelProperty("性别")
    private String sex;

    @ExcelProperty("角色")
    private String roleName;

    @ExcelProperty("入职时间")
    private String hireDate;

    @ExcelProperty("人员类型")
    private String infoType;

    @ExcelProperty("用户邮箱")
    private String email;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("转正日期")
    private String formalDate;

    @ExcelProperty("身份证号码")
    private String identityCard;

    @ExcelProperty("户籍地址")
    private String residentialAddress;

    @ExcelProperty("居住地址")
    private String liveAddress;

    @ExcelProperty("紧急联系人")
    private String contact;

    @ExcelProperty("紧急联系人电话")
    private String contractPhone;

    @ExcelProperty("试用工资比例（%）")
    private BigDecimal probationSalaryRatio;

    @ExcelProperty("基础工资")
    private BigDecimal baseSalary;

    @ExcelProperty("是否有试用期")
    private String hasProbation;

    @ExcelProperty("工资发放方式")
    private String salaryPaymentMethod;

    @ExcelProperty("银行卡号")
    private String bankAccountNumber;

    @ExcelProperty("银行卡信息")
    private String bankAccount;

    @ExcelProperty("身份证有效期")
    private String cardPeriod;

    @ExcelProperty("民族")
    private String nation;

    @ExcelProperty("籍贯")
    private String birthplace;

    @ExcelProperty("婚姻状况")
    private String maritalStatus;

    @ExcelProperty("政治面貌")
    private String political;

    @ExcelProperty("毕业院校")
    private String graduateSchool;

    @ExcelProperty("专业")
    private String profession;

    @ExcelProperty("出生日期")
    private String birthDate;

    @ExcelProperty("工作地点")
    private String workArea;

    @ExcelProperty("试用期时长(月)")
    private Integer tryTime;

    @ExcelProperty("试用期截止日")
    private String tryDate;

    @ExcelProperty("背调人")
    private String referenceChecher;

    @ExcelProperty("介绍人")
    private String referrer;

    @ExcelProperty("工龄（年）")
    private Integer serviceYear;

    @ExcelProperty("工龄（月）")
    private Integer serviceMonth;

    @ExcelProperty("合同类型")
    private String contractType;

    @ExcelProperty("签订年限")
    private Integer signYear;

    @ExcelProperty("合同到期日")
    private String expiryDate;

    @ExcelProperty("车牌号")
    private String plateNumber;

    @ExcelProperty("证书1")
    private String certificate1;

    @ExcelProperty("证书2")
    private String certificate2;

    @ExcelProperty("试用工资")
    private BigDecimal trySalary;

    @ExcelProperty("转正工资")
    private BigDecimal formalSalary;

    @ExcelProperty("绩效")
    private BigDecimal result;

    @ExcelProperty("津贴")
    private BigDecimal subsidies;

    @ExcelProperty("工作服尺码")
    private String clothesSize;

    @ExcelProperty("工作鞋尺码")
    private String shoesSize;

    @ExcelProperty("是否离职")
    private String isResigned;

    @ExcelProperty("离职时间")
    private String resignTime;

    @ExcelProperty("账号状态")
    private String status;

}
