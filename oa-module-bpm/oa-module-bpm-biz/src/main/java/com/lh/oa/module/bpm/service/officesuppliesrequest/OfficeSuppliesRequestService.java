package com.lh.oa.module.bpm.service.officesuppliesrequest;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestCreateVo;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestPageReqVo;
import com.lh.oa.module.bpm.controller.admin.officesuppliesrequest.vo.OfficeSuppliesRequestRespVo;
import com.lh.oa.module.bpm.dal.dataobject.officesuppliesrequest.OfficeSuppliesRequestDO;


public interface OfficeSuppliesRequestService {

    Long createOfficeSu(OfficeSuppliesRequestCreateVo createReqVO);

    void updateOfficeSuResult(Long id, Integer result);

    OfficeSuppliesRequestRespVo getOfficeSu(Long id);

    PageResult<OfficeSuppliesRequestDO> getOfficeSuPage(Long userId, OfficeSuppliesRequestPageReqVo pageVO);

}
