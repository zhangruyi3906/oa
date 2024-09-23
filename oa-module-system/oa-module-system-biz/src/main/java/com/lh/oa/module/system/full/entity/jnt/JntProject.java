package com.lh.oa.module.system.full.entity.jnt;

import com.lh.oa.module.system.full.entity.base.BaseEntity;
import com.lh.oa.module.system.full.enums.jnt.OperateTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.ProjectCategoryEnum;
import com.lh.oa.module.system.full.enums.jnt.ProjectTypeEnum;
import com.lh.oa.module.system.full.enums.jnt.SourceEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class JntProject extends BaseEntity {
    private OperateTypeEnum operateType;
    private String ids;
    private int oaProjectId;
    private SourceEnum source;

    private String simpleName;
    private String simpleDescription;
    private ProjectTypeEnum type;
    private ProjectCategoryEnum category;
    private int effectGraphFileId;
    private String effectGraphFileUrl;
    private int planarGraphFileId;
    private String planarGraphFileUrl;
    private int planStartTime;
    private int planEndTime;
    private String directorName;
    private String directorMobile;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String constructUnit;
    private String workUnit;
    private String superviseUnit;
    private String designUnit;
    private String surveyUnit;
    private int microWebsiteId;
    private int orgId;
    private String orgName;
    private int orderNumber;



}