package com.lh.oa.module.system.materialApply.controller;

import com.lh.oa.module.system.materialApply.service.MaterialApplyStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/material/apply")
public class MaterialApplyStorageController {
    @Autowired
    private MaterialApplyStorageService materialApplyStorageService;
    @PostMapping("/materialApplyStorage")
    public String materialApplyStorage(@RequestBody Map<String, Object> variableInstances){
         return materialApplyStorageService.materialApplyStorage(variableInstances);
    }
}
