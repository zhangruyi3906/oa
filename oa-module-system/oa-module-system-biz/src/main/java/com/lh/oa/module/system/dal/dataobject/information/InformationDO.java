package com.lh.oa.module.system.dal.dataobject.information;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.lh.oa.module.system.controller.admin.user.vo.user.UserPageItemRespVO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 员工信息 DO
 *
 * @author
 */
@TableName("user_information")
//("user_information_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InformationDO extends BaseDO {

    /**
     * 员工唯一标识符
     */
    @TableId
    private Long id;
    /**
     * 员工ID
     */
    private Long userId;
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 入职时间
     */
    private LocalDateTime hireDate;
    /**
     * 是否有试用期（true表示有试用期，false表示没有试用期）
     */
    private Boolean hasProbation;
    /**
     * 试用期工资比例
     */
    private BigDecimal probationSalaryRatio;
    /**
     * 工资发放方式
     */
    private String salaryPaymentMethod;
    /**
     * 银行卡信息
     */
    private String bankAccount;

    /**
     * 员工基础工资
     */
    private BigDecimal baseSalary;
    /**
     * 记录创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 记录最后更新时间
     */
    private LocalDateTime updatedAt;
    /**
     * 身份证号码
     */
    private String identityCard;
    /**
     * 1-内聘人员 2-外聘人员
     */
    private Integer infoType;
    /**
     * 是否异地打卡
     */
    private Boolean isOffsiteAttendance;


    /**
     * 离职时间
     */
    private LocalDateTime resignTime;
    /**
     * 是否离职
     */
    private Boolean isResigned;

    private String workArea;
    private Date formalDate;
    private Integer tryTime;
    private Date tryDate;
    private String bankAccountNumber;
    private String contact;
    private String contractPhone;
    private String nation;
    private Date cardPeriod;
    private String graduateSchool;
    private String qualifications;
    private String profession;
    private Date birthDate;
    private String birthplace;
    private String maritalStatus;
    private String political;
    private String residentialAddress;
    private String liveAddress;
    private String referenceChecher;
    private String referrer;
    private Integer serviceYear;
    private Integer serviceMonth;
    private String contractType;
    private Integer signYear;
    private Date expiryDate;
    private String plateNumber;
    private String certificate1;
    private String certificate2;
    private BigDecimal trySalary;
    private BigDecimal formalSalary;
    private BigDecimal result;
    private BigDecimal subsidies;
    private String clothesSize;
    private String shoesSize;

    @TableField(exist = false)
    private AdminUserDO adminUserDo;

    @TableField(exist = false)
    private UserPageItemRespVO.Dept dept;

    @TableField(exist = false)
    private Long deptId;

    @TableField(exist = false)
    private String deptName;

    public Long getDeptId() {
        return dept == null ? deptId : dept.getId();
    }
    public String getDeptName() {
        return dept == null ? deptName : dept.getName();
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "InformationDO{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", hireDate=" + hireDate +
                ", hasProbation=" + hasProbation +
                ", probationSalaryRatio=" + probationSalaryRatio +
                ", salaryPaymentMethod='" + salaryPaymentMethod + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", baseSalary=" + baseSalary +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", identityCard='" + identityCard + '\'' +
                ", infoType=" + infoType +
                ", resignTime=" + resignTime +
                ", isResigned=" + isResigned +
                ", adminUserDo=" + adminUserDo +
                ", dept=" + dept +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                '}';
    }
}
