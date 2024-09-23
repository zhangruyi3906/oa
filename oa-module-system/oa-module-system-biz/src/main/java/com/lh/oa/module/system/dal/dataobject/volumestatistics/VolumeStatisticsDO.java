package com.lh.oa.module.system.dal.dataobject.volumestatistics;

import com.lh.oa.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * 员工方量统计 DO
 *
 * @author
 */
@TableName("user_volume_statistics")
//("user_volume_statistics_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VolumeStatisticsDO extends BaseDO {

    /**
     * 记录编号
     */
    @TableId
    private Long id;
    /**
     * 员工编号
     */
    private Long userId;
    /**
     * 项目编号
     */
    private Long projectId;
    /**
     * 方量
     */
    private Long volume;
    /**
     * 日期
     */
    private Date volumeDate;

}
