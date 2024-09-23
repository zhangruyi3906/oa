package com.lh.oa.module.system.controller.admin.userProject.vo;

import com.lh.oa.module.system.full.enums.jnt.ProjectSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 人员项目 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class UserProjectBaseVO {

    @Schema(description = "人员id")
    private Long userId;

    @Schema(description = "人员名称")
    private String userName;

    @Schema(description = "项目id")
//    @NotNull(message = "项目id不能为空")
    private Long projectId;

    @Schema(description = "人员名称")
    private String projectName;

    @Schema(description = "人员是否已经离开（是0，否1）")
    private Byte status;

    @Schema(description = "离开时间")
    private Date leaveTime;

    private Integer type; //0 管理员，1 施工员

    private Integer isRecord; //0 考勤，1 不考勤

    /**
     * 入场时间
     */
    private Date inTime;
    /**
     * 转场时间
     */
    private Date outTime;
    /**
     * 来源
     */
    private ProjectSourceEnum source;
    private List<UserProjectCreateReqVO.User> list;

}
