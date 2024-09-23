//package com.lh.oa.module.system.api.companyCarBorrow;
//
//import com.lh.oa.framework.common.pojo.JsonResult;
//import com.lh.oa.module.system.dal.dataobject.companyCarBorrow.CompanyCarBorrowRes;
//import com.lh.oa.module.system.service.impl.ICompanyCarBorrowService;
//import org.apache.dubbo.config.annotation.DubboService;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
//import static com.lh.oa.module.system.enums.CarBorrowConstants.VERSION;
//
//@RestController // 提供 RESTful API 接口，给 Feign 调用
//@DubboService(version = VERSION) // 提供 Dubbo RPC 接口，给 Dubbo Consumer 调用
//@Validated
//public class CarBorrowServiceImpl implements CompanyCarBorrowService{
//    @Resource
//    private ICompanyCarBorrowService companyCarBorrowService;
//    @Override
//    public JsonResult saveOrUpdate(CompanyCarBorrow companyCarBorrow) {
//        int insert = companyCarBorrowService.insert(companyCarBorrow);
//        return JsonResult.success(insert);
//    }
//
//    @Override
//    public JsonResult get(Long id) {
//        CompanyCarBorrowRes companyCarBorrowRes = companyCarBorrowService.selectById(id);
//        return JsonResult.success(companyCarBorrowRes);
//    }
//}
