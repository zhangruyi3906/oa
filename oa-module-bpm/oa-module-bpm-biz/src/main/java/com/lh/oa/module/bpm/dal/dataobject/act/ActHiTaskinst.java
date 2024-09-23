package com.lh.oa.module.bpm.dal.dataobject.act;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* @author tanghanlin
* @since 2023-12-14
*/
@Getter
@Setter
@Accessors(chain = true)
@TableName("act_hi_taskinst")
public class ActHiTaskinst implements Serializable {

    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;

    @TableField("PROC_INST_ID_")
    private String procInstId;

    @TableField("ASSIGNEE_")
    private String assignee;

    @TableField("END_TIME_")
    private Date endTime;

}
