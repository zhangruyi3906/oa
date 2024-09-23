package com.lh.oa.module.system.dal.dataobject.companyCarBorrow;

import com.lh.oa.module.system.api.companyCarBorrow.CompanyCarBorrow;
import com.lh.oa.module.system.api.companyCar.CompanyCar;
import lombok.Data;

@Data
public class CompanyCarBorrowRes extends CompanyCarBorrow {

    private CompanyCar companyCar;
}
