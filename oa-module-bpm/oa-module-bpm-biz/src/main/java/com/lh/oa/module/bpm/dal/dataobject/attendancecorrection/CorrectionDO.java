package com.lh.oa.module.bpm.dal.dataobject.attendancecorrection;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;

/**
 * 补卡流程 DO
 *
 * @author 狗蛋
 */
@TableName("attendance_correction")
//("attendance_correction_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionDO extends BaseDO {

    /**
     * 补卡申请ID
     */
    @TableId
    private Long id;
    /**
     * 申请人ID
     */
    private Long userId;
    /**
     * 申请人姓名
     */
    private String userName;
    /**
     * 补卡原因
     */
    private String reason;
    /**
     * 补卡时间
     */
    private Date correctionTime;
    /**
     * 月份
     */
    private String month;
    /**
     * 补卡类型
     */
    private String type;
    /**
     * 流程实例编号
     */
    private String processInstanceId;
    /**
     * 审批状态
     */
    private Integer approvalStatus;

}
