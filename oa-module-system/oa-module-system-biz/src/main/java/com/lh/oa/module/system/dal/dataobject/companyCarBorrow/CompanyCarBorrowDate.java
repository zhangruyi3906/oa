package com.lh.oa.module.system.dal.dataobject.companyCarBorrow;

import lombok.Data;

import java.util.Date;
@Data
public class CompanyCarBorrowDate {

    private Date borrowedTime;

    private Date expectTime;
}
