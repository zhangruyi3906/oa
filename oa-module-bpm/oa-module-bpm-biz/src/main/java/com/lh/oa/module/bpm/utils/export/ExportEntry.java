package com.lh.oa.module.bpm.utils.export;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ExportEntry {
    //内容
    private List<List> objData;
    private String sheetName;
    //标题
    private List<String> columns;
    private Set<Integer> mergeIndexSet;
}
