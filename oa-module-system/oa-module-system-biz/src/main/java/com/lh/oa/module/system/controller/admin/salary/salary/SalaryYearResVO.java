package com.lh.oa.module.system.controller.admin.salary.salary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryYearResVO {
    //最新工资
    private BigDecimal newSalary;
    //年度总工资
    private BigDecimal yearSalary;
    //月平均工资
    private BigDecimal avgSalary;

    private List<String> monthList;
    private List<BigDecimal> salaryList;
}
