package com.lh.oa.module.system.dal.dataobject.orderMeet;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.lh.oa.framework.common.pojo.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 * @author ${author}
 * @since 2023-09-11
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderMeetQuery extends PageParam {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("room_id")
    private Long roomId;
    /**
     * 会议室名
     */
    @TableField("room_name")
    private String roomName;
    /**
     * 会议开始时间
     */
    @TableField("meet_start_time")
    private Date meetStartTime;
    /**
     * 会议结束时间
     */
    @TableField("meet_end_time")
    private Date meetEndTime;
    /**
     * 预约日期
     */
    @TableField("order_date")
    private Date orderDate;
    /**
     * 会议标题
     */
    private String title;
    /**
     * 会议描述
     */
    private String description;
    /**
     * 组织者id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 组织者
     */
    private String username;

    private Integer status;
}