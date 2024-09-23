package com.lh.oa.module.system.service.logger;

import com.lh.oa.framework.common.pojo.PageResult;
import com.lh.oa.module.system.api.logger.dto.LoginLogCreateReqDTO;
import com.lh.oa.module.system.controller.admin.logger.vo.loginlog.LoginLogExportReqVO;
import com.lh.oa.module.system.controller.admin.logger.vo.loginlog.LoginLogPageReqVO;
import com.lh.oa.module.system.convert.logger.LoginLogConvert;
import com.lh.oa.module.system.dal.dataobject.logger.LoginLogDO;
import com.lh.oa.module.system.dal.mysql.logger.LoginLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * 登录日志 Service 实现
 */
@Service
@Validated
public class LoginLogServiceImpl implements LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    @Override
    public PageResult<LoginLogDO> getLoginLogPage(LoginLogPageReqVO reqVO) {
        return loginLogMapper.selectPage(reqVO);
    }

    @Override
    public List<LoginLogDO> getLoginLogList(LoginLogExportReqVO reqVO) {
        return loginLogMapper.selectList(reqVO);
    }

    @Override
    public void createLoginLog(LoginLogCreateReqDTO reqDTO) {
        LoginLogDO loginLog = LoginLogConvert.INSTANCE.convert(reqDTO);
        loginLogMapper.insert(loginLog);
    }

}
