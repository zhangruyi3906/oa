package com.lh.oa.module.system.dal.mysql.worklog;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.framework.mybatis.core.mapper.BaseMapperX;
import com.lh.oa.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogExportReqVO;
import com.lh.oa.module.system.controller.admin.worklog.vo.WorkLogPageReqVO;
import com.lh.oa.module.system.dal.dataobject.worklog.WorkLogDO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 员工工作日志 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface WorkLogMapper extends BaseMapperX<WorkLogDO> {

    default PageResult<WorkLogDO> selectPage(Set<Long> deptIdSet, WorkLogPageReqVO reqVO) {
        Date start = null;
        Date end = null;
        if(reqVO.getStartTime()!=null){
             start = new Date(reqVO.getStartTime());
        }
        if(reqVO.getEndTime()!=null){
             end = new Date(reqVO.getEndTime());
        }
        LambdaQueryWrapperX<WorkLogDO> queryWrapperX = new LambdaQueryWrapperX<WorkLogDO>()
                .eqIfPresent(WorkLogDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(WorkLogDO::getLogDate, start, end)
                .eqIfPresent(WorkLogDO::getCreatedAt, reqVO.getCreatedAt())
                .eqIfPresent(WorkLogDO::getDescription, reqVO.getDescription())
                .eqIfPresent(WorkLogDO::getIsEditable, reqVO.getIsEditable())
                .inIfPresent(WorkLogDO::getDeptId, deptIdSet)
                .likeIfPresent(WorkLogDO::getUserName, reqVO.getUserName())
                .orderByDesc(WorkLogDO::getLogDate)
                .orderByDesc(WorkLogDO::getSubmitTime);
        if (ObjectUtils.isNotEmpty(reqVO.getUserName()) || ObjectUtils.isEmpty(reqVO.getUserId())) {
            queryWrapperX.isNotNull(WorkLogDO::getLogContent);
        }
        return selectPage(reqVO, queryWrapperX);
    }

    default List<WorkLogDO> selectList(WorkLogExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WorkLogDO>()
                .eqIfPresent(WorkLogDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(WorkLogDO::getLogDate, reqVO.getLogDate())
                .eqIfPresent(WorkLogDO::getLogContent, reqVO.getLogContent())
                .eqIfPresent(WorkLogDO::getCreatedAt, reqVO.getCreatedAt())
                .eqIfPresent(WorkLogDO::getDescription, reqVO.getDescription())
                .eqIfPresent(WorkLogDO::getIsEditable, reqVO.getIsEditable())
                .eqIfPresent(WorkLogDO::getDeptId, reqVO.getDeptId())
                .orderByDesc(WorkLogDO::getId));
    }

    @Select("SELECT * FROM system_work_log WHERE create_time < DATE_SUB(NOW(), INTERVAL 8 DAY)")
    List<WorkLogDO> selectWorkLogDOsCreatedAfterEightDays();
    @Insert("INSERT INTO system_work_log\n" +
            "  (user_id, log_date, log_content, created_at, description, is_editable, dept_id, tenant_id, deleted, create_time, update_time, creator, updater, user_name)\n" +
            "  SELECT\n" +
            "  id, CURDATE(), NULL, NOW(), NULL, true, dept_id, tenant_id, false, NOW(), NOW(), 'System', 'System', nickname\n" +
            "  FROM\n" +
            "  system_users where deleted = 0;")
    void insertForAllUsers();

}
