package com.lh.oa.module.system.controller.admin.dept.vo.dept;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptPressVO {
    private String label;
    private Long parentId;
    private Long value;
    private List<DeptPressVO> children = new ArrayList<>();
}
