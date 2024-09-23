package com.lh.oa.module.system.service.information;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.information.vo.InformationCreateReqVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationExportReqVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationPageReqVO;
import com.lh.oa.module.system.controller.admin.information.vo.InformationUpdateReqVO;
import com.lh.oa.module.system.controller.admin.salarysettlement.vo.MonthAttendance;
import com.lh.oa.module.system.dal.dataobject.information.InformationDO;
import com.lh.oa.module.system.dal.dataobject.userProject.UserProjectDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 员工信息 Service 接口
 *
 * @author
 */
public interface InformationService {

    /**
     * 创建员工信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInformation(@Valid InformationCreateReqVO createReqVO);

    /**
     * 更新员工信息
     *
     * @param updateReqVO 更新信息
     */
    void updateInformation(@Valid InformationUpdateReqVO updateReqVO);

    /**
     * 删除员工信息
     *
     * @param id 编号
     */
    void deleteInformation(Long id);

    /**
     * 获得员工信息
     *
     * @param id 编号
     * @return 员工信息
     */
    InformationDO getInformation(Long id);

    /**
     * 通过员工Id获得员工信息
     *
     * @param userId 员工编号
     * @return 员工信息
     */
    InformationDO getInformationByUserId(Long userId);

    /**
     * 获得员工信息列表
     *
     * @param ids 编号
     * @return 员工信息列表
     */
    List<InformationDO> getInformationList(Collection<Long> ids);

    /**
     * 获得员工信息分页
     *
     * @param pageReqVO 分页查询
     * @return 员工信息分页
     */
    PageResult<InformationDO> getInformationPage(InformationPageReqVO pageReqVO);

    List<UserProjectDO> getInforProject(InformationPageReqVO pageReqVO);

    /**
     * 获得员工信息列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 员工信息列表
     */
    List<InformationDO> getInformationList(InformationExportReqVO exportReqVO);

    List<InformationDO> selectListInDeptIdsOrUserIds(MonthAttendance createReqVO);

    List<InformationDO> selectListByUserIds(Set<Long> userIds);
}
