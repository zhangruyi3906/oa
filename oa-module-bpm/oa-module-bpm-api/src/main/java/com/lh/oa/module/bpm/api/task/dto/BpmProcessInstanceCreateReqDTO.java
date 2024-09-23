package com.lh.oa.module.bpm.api.task.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * 流程实例的创建 Request DTO
 *
 * @author
 */
@Data
public class BpmProcessInstanceCreateReqDTO {

    /**
     * 流程定义的标识
     */
    @NotEmpty(message = "流程定义的标识不能为空")
    private String processDefinitionKey;
    /**
     * 变量实例
     */
    private Map<String, Object> variables;

    /**
     * 业务的唯一标识
     *
     * 例如说，请假申请的编号。通过它，可以查询到对应的实例
     */
    @NotEmpty(message = "业务的唯一标识")
    private String businessKey;

    public BpmProcessInstanceCreateReqDTO() {
    }

    public BpmProcessInstanceCreateReqDTO(@NotEmpty(message = "流程定义的标识不能为空") String processDefinitionKey,
                                          Map<String, Object> variables,
                                          @NotEmpty(message = "业务的唯一标识") String businessKey) {
        this.processDefinitionKey = processDefinitionKey;
        this.variables = variables;
        this.businessKey = businessKey;
    }
}
