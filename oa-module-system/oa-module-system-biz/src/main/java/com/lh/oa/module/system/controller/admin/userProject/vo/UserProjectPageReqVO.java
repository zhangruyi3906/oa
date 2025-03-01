package com.lh.oa.module.system.controller.admin.userProject.vo;

import com.lh.oa.framework.common.pojo.PageParam;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Schema(description = "管理后台 - 人员项目分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserProjectPageReqVO extends PageParam {

    @Schema(description = "人员id")
    private Long userId;

    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "人员是否已经离开（是1，否0）")
    private Byte status;

    @Schema(description = "开始时间，格式为10位的时间戳，数字类型的")
    private Long startTime;

    @Schema(description = "结束时间，格式为10位的时间戳，数字类型的")
    private Long endTime;

    private Integer type;

    private Integer isRecord;

    private String userName;

    /**
     * 入场时间
     */
    private Date inTime;
    /**
     * 转场时间
     */
    private Date outTime;



    private AdminUserDO adminUserDO;


    private InformationDO informationDO;

    private Boolean isResigned;

    private Integer infoType;


}
