package com.lh.oa.module.system.job.demo;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class DemoJob{

    @XxlJob("demoJob")
    public void execute() {
        System.out.println("美滋滋");
    }


    @XxlJob("MyJobHandler")
    public ReturnT<String> execute(String param) {


        return ReturnT.SUCCESS;
    }


}
