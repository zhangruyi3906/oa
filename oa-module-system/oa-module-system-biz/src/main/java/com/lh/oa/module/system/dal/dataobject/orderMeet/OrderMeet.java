package com.lh.oa.module.system.dal.dataobject.orderMeet;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2023-09-11
 */
@TableName("order_meet")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMeet extends Model<OrderMeet> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("room_id")
    @NotNull(message = "会议室id不能为空")
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
    @NotNull(message = "会议开始时间不能为空")
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
    /**
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 租户编号
     */

    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
    private String creator;
    private String updater;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
