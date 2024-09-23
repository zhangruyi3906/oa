package com.lh.oa.module.system.controller.admin.user.vo.user;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.lh.oa.framework.excel.core.annotations.DictFormat;
import com.lh.oa.framework.excel.core.convert.DictConvert;
import com.lh.oa.module.system.enums.DictTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 用户 Excel 导入 VO
 */
@Data
@Builder
@HeadStyle(fillForegroundColor = 1)
@HeadFontStyle(color = 8)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class UserImportExcelVO {

    @ExcelProperty("账号")
    @HeadFontStyle(color = 2)
    private String username;

    @ExcelProperty("姓名")
    @HeadFontStyle(color = 2)
    private String nickname;

    @ExcelProperty("职级")
    @HeadFontStyle(color = 2)
    private String level;

    @ExcelProperty("一级部门")
    @HeadFontStyle(color = 2)
    @ColumnWidth(value = 30)
    private String parentDeptName;

    @ExcelProperty("部门")
    @ColumnWidth(value = 30)
    @HeadFontStyle(color = 2)
    private String deptName;

    @ExcelProperty("手机号")
    @HeadFontStyle(color = 2)
    private String mobile;

    @ExcelProperty("岗位")
    @HeadFontStyle(color = 2)
    private String postName;

    @ExcelProperty(value = "性别")
    @HeadFontStyle(color = 2)
    private String sexStr;

    @ExcelProperty("角色")
    @HeadFontStyle(color = 2)
    private String roleName;

    @ExcelProperty("入职时间")
    @HeadFontStyle(color = 2)
    private String hireDateStr;

    @ExcelProperty("人员类型")
    @HeadFontStyle(color = 2)
    private String infoTypeStr;

    @ExcelProperty("用户邮箱")
    private String email;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("转正日期")
    @HeadFontStyle(color = 16)
    private String formalDateStr;

    @ExcelProperty("身份证号码")
    @HeadFontStyle(color = 16)
    private String identityCard;

    @ExcelProperty("户籍地址")
    @HeadFontStyle(color = 16)
    private String residentialAddress;

    @ExcelProperty("居住地址")
    @HeadFontStyle(color = 16)
    private String liveAddress;

    @ExcelProperty("紧急联系人")
    @HeadFontStyle(color = 16)
    private String contact;

    @ExcelProperty("紧急联系人电话")
    @HeadFontStyle(color = 16)
    private String contractPhone;

    @ExcelProperty("试用工资比例（%）")
    private BigDecimal probationSalaryRatio;

    @ExcelProperty("基础工资")
    private BigDecimal baseSalary;

    @ExcelProperty("是否有试用期")
    private String hasProbationStr;

    @ExcelProperty("工资发放方式")
    private String salaryPaymentMethod;

    @ExcelProperty("银行卡号")
    private String bankAccountNumber;

    @ExcelProperty("银行卡信息")
    private String bankAccount;

    @ExcelProperty("身份证有效期")
    private String cardPeriodStr;

    @ExcelProperty("民族")
    @ColumnWidth(value = 15)
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
    private String birthDateStr;

    @ExcelProperty("工作地点")
    private String workArea;

    @ExcelProperty("试用期时长(月)")
    private Integer tryTime;

    @ExcelProperty("试用期截止日")
    private String tryDateStr;

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
    private String expiryDateStr;

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
    private String isResignedStr;

    @ExcelProperty("离职时间")
    private String resignTimeStr;

    @Deprecated
    @ExcelIgnore
    @ExcelProperty("学历")
    private String qualifications;

    @Deprecated
    @ExcelIgnore
    @ExcelProperty(value = "账号状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @ExcelProperty("提示信息")
    @ContentStyle(wrapped = BooleanEnum.TRUE)
    private String msg = "所有日期的格式：年-月-日，例如：2023-01-01；\n导入失败时失败信息会回写在这里，请勿主动填写";

    public Boolean isNotEmpty() {
        return StringUtils.isNotBlank(this.getUsername()) || StringUtils.isNotBlank(this.getNickname())
                || StringUtils.isNotBlank(this.getLevel()) || StringUtils.isNotBlank(this.getDeptName())
                || StringUtils.isNotBlank(this.getMobile()) || StringUtils.isNotBlank(this.getPostName())
                || StringUtils.isNotBlank(this.getSexStr()) || StringUtils.isNotBlank(this.getRoleName())
                || Objects.nonNull(this.getHireDateStr()) || StringUtils.isNotBlank(this.getInfoTypeStr())
                || StringUtils.isNotBlank(this.getParentDeptName());
    }
    
}
