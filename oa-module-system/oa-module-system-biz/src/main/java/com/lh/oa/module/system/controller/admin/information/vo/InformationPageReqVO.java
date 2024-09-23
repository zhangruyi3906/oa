package com.lh.oa.module.system.controller.admin.information.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserPageItemRespVO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import static com.lh.oa.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 员工信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InformationPageReqVO extends PageParam {

    @Schema(description = "员工ID")
    private Long userId;

    @Schema(description = "员工姓名")
    private String name;

    @Schema(description = "入职时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] hireDate;

    @Schema(description = "是否有试用期（true表示有试用期，false表示没有试用期）")
    private Byte hasProbation;

    @Schema(description = "试用期工资比例")
    private BigDecimal probationSalaryRatio;

    @Schema(description = "工资发放方式")
    private String salaryPaymentMethod;

    @Schema(description = "银行卡信息")
    private String bankAccount;


    @Schema(description = "员工基础工资")
    private BigDecimal baseSalary;

    @Schema(description = "记录创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "记录最后更新时间")
    private LocalDateTime updatedAt;

    @Schema(description = "身份证号码")
    private String identityCard;

    private Long startTime;

    private Long endTime;

    private Integer infoType;

    @Schema(description = "离职时间")
    private LocalDateTime resignTime;

    @Schema(description = "是否离职")
    private Boolean isResigned = false;

    @Schema(description = "工作地点")
    private Date workArea;

    @Schema(description = "转正日期")
    private Date formalDate;

    @Schema(description = "试用期时长(月)")
    private Integer tryTime;

    @Schema(description = "试用期截止日")
    private Date tryDate;

    @Schema(description = "银行卡号")
    private String bankAccountNumber;

    @Schema(description = "紧急联系人")
    private String contact;

    @Schema(description = "联系人电话")
    private String contractPhone;

    @Schema(description = "民族")
    private String nation;

    @Schema(description = "身份证有效期")
    private Date cardPeriod;

    @Schema(description = "毕业院校")
    private String graduateSchool;

    @Schema(description = "学历")
    private String qualifications;

    @Schema(description = "专业")
    private String profession;

    @Schema(description = "出生日期")
    private Date birthDate;

    @Schema(description = "籍贯")
    private String birthplace;

    @Schema(description = "婚姻状况")
    private String maritalStatus;

    @Schema(description = "政治面貌")
    private String political;

    @Schema(description = "户籍地址")
    private String residentialAddress;

    @Schema(description = "居住地址")
    private String liveAddress;

    @Schema(description = "背调人及背调详情")
    private String referenceChecher;

    @Schema(description = "介绍人")
    private String referrer;

    @Schema(description = "工龄（年）")
    private Integer serviceYear;

    @Schema(description = "工龄（月）")
    private Integer serviceMonth;

    @Schema(description = "合同类型")
    private String contractType;

    @Schema(description = "签订年限")
    private Integer signYear;

    @Schema(description = "合同到期日")
    private Date expiryDate;

    @Schema(description = "车牌号")
    private String plateNumber;

    @Schema(description = "证书1")
    private String certificate1;

    @Schema(description = "证书2")
    private String certificate2;

    @Schema(description = "试用工资")
    private BigDecimal trySalary;

    @Schema(description = "转正工资")
    private BigDecimal formalSalary;

    @Schema(description = "绩效")
    private BigDecimal result;

    @Schema(description = "津贴")
    private BigDecimal subsidies;

    @Schema(description = "工作服尺码")
    private String clothesSize;

    @Schema(description = "工作鞋尺码")
    private String shoesSize;

    private AdminUserDO adminUserDo;

    private UserPageItemRespVO.Dept dept;


    private Long deptId;

    private String deptName;

    private Set<Long> deptIds;


}
