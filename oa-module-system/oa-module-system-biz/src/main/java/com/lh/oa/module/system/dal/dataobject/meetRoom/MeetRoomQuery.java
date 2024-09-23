package com.lh.oa.module.system.dal.dataobject.meetRoom;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.lh.oa.framework.common.pojo.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ${author}
 * @since 2023-09-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetRoomQuery extends PageParam {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 会议室名
     */
    @TableField("room_name")
    private String roomName;
    /**
     * 可容纳人数
     */
    private String contain;
    /**
     * 描述
     */
    private String description;
}