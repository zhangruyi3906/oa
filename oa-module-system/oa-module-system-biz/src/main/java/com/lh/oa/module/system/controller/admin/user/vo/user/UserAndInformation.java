package com.lh.oa.module.system.controller.admin.user.vo.user;

import com.lh.oa.framework.common.validation.Mobile;
import com.lh.oa.module.system.full.enums.jnt.ProjectSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Data
public class UserAndInformation {
    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotBlank(message = "用户账号不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$", message = "用户账号由 数字、字母 组成")
    @Size(min = 4, max = 30, message = "用户账号长度为 4-30 个字符")
    private String username;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    @NotBlank(message = "用户姓名不能为空")
    private String nickname;

    @Schema(description = "备注", example = "我是一个用户")
    private String remark;

    @Schema(description = "部门ID")
    @NotNull(message = "部门不能为空")
    private Long deptId;

    @Schema(description = "职级")
    @NotBlank(message = "职级不能为空")
    private String level;

    @Schema(description = "岗位编号数组", example = "1")
    @NotEmpty(message = "岗位不能为空")
    private Set<Long> postIds;

    @Schema(description = "用户邮箱", example = "")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过 50 个字符")
    private String email;

    @Schema(description = "手机号码", example = "15601691300")
    @Mobile
    private String mobile;

    @Schema(description = "用户性别，参见 SexEnum 枚举类", example = "1")
    @NotNull(message = "性别不能为空")
    private Integer sex;

    @Schema(description = "用户头像", example = "")
    private String avatar;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
//    @NotEmpty(message = "密码不能为空")
    @Length(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;
    @Schema(description = "是否异地打卡")
    private Boolean isOffsiteAttendance;

    /**
     * 用户角色
     */
    private Set<Long> userRoleIdList = new HashSet<>();

//information

    @Schema(description = "入职时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime hireDate;

    @Schema(description = "是否有试用期（true表示有试用期，false表示没有试用期）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean hasProbation;

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

    @Schema(description = "部门人员/项目人员/外聘人员：0/1/2")
    private Integer infoType;

    @Schema(description = "离职时间")
    private LocalDateTime resignTime;

    @Schema(description = "是否离职")
    private Boolean isResigned = false;


    @Schema(description = "工作地点")
    private String workArea;
    @Schema(description = "转正日期")
    private Date formalDate;
    @Schema(description = "试用期时长(月)")
    private Integer tryTime;
    @Schema(description = "试用期截止日")
    private Date tryDate;
    @Schema(description = "银行卡号")
    @Pattern(regexp = "^[0-9]{1,20}$", message = "银行卡不能超过20位")
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
    @Schema(description = "来源")
    private ProjectSourceEnum source;

}