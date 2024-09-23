package com.lh.oa.module.system.service.salary;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryCreateVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryExportExcelVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryExportVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryImportExcelVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryMonthSimpleVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryPageReqVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryResVO;
import com.lh.oa.module.system.controller.admin.salary.salary.SalaryYearResVO;
import com.lh.oa.module.system.dal.dataobject.salary.SalaryDO;

import java.util.List;

public interface SalaryService {
    PageResult<SalaryDO> getSalaryPage(SalaryPageReqVO salaryPageReqVO);

    void createSalaryList(List<SalaryCreateVO> salaryCreateVOList);

    List<SalaryImportExcelVO> importSalaryList(String month, List<SalaryImportExcelVO> list, Boolean updateSupport);

    List<SalaryExportExcelVO> exportSalary(SalaryExportVO salaryExportVO);

    List<SalaryResVO> getSalaryListByMobileAndMonth(Long userId, String mobile, String month);

    SalaryYearResVO getSalaryListByMobileAndYear(Long userId, String mobile, String year);

    List<SalaryMonthSimpleVO> getSalaryMonthList(Long userId);

    void deleteByMonth(String month);
}
