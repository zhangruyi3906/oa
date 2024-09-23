package com.lh.oa.module.bpm.enums;

import com.lh.oa.framework.common.exception.ErrorCode;

/**
 * Bpm 错误码枚举类
 *
 * bpm 系统，使用 1-009-000-000 段
 */
public interface ErrorCodeConstants {

    // ==========  通用流程处理 模块 1009000000 ==========
    ErrorCode HIGHLIGHT_IMG_ERROR = new ErrorCode(1009000002, "获取高亮流程图异常");

    // ========== OA 流程模块 1009001000 ==========
    ErrorCode OA_LEAVE_NOT_EXISTS = new ErrorCode(1009001001, "请假申请不存在");
    ErrorCode OA_PM_POST_NOT_EXISTS = new ErrorCode(1009001002, "项目经理岗位未设置");
    ErrorCode OA_DEPART_PM_POST_NOT_EXISTS = new ErrorCode(1009001009, "部门的项目经理不存在");
    ErrorCode OA_BM_POST_NOT_EXISTS = new ErrorCode(1009001004, "部门经理岗位未设置");
    ErrorCode OA_DEPART_BM_POST_NOT_EXISTS = new ErrorCode(1009001005, "部门的部门经理不存在");
    ErrorCode OA_HR_POST_NOT_EXISTS = new ErrorCode(1009001006, "HR岗位未设置");
    ErrorCode OA_DAY_LEAVE_ERROR = new ErrorCode(1009001007, "请假天数必须>=1");
    ErrorCode SUPPLY_NOT_EXISTS_ERROR = new ErrorCode(1009001008, "申请流程数据不存在");


    // ========== 流程模型 1009002000 ==========
    ErrorCode MODEL_KEY_EXISTS = new ErrorCode(1009002006, "已经存在流程标识为【{}】的流程");
    ErrorCode MODEL_NAME_EXISTS = new ErrorCode(1009002000, "已经存在流程名字为【{}】的流程");
    ErrorCode MODEL_NOT_EXISTS = new ErrorCode(1009002001, "流程模型不存在");
    ErrorCode MODEL_KEY_VALID = new ErrorCode(1009002002, "流程标识格式不正确，需要以字母或下划线开头，后接任意字母、数字、中划线、下划线、句点！");
    ErrorCode MODEL_DEPLOY_FAIL_FORM_NOT_CONFIG = new ErrorCode(1009002003, "部署流程失败，原因：流程表单未配置，请点击【修改流程】按钮进行配置");
    ErrorCode MODEL_DEPLOY_FAIL_TASK_ASSIGN_RULE_NOT_CONFIG = new ErrorCode(1009002004, "部署流程失败，" +
            "原因：用户任务({})未配置分配规则，请点击【修改流程】按钮进行配置");
    ErrorCode MODEL_DEPLOY_FAIL_TASK_INFO_EQUALS = new ErrorCode(1009002005, "流程定义部署失败，原因：信息未发生变化");
    ErrorCode MODEL_IS_ERROR = new ErrorCode(1009002007, "流程模型异常");
    ErrorCode MODEL_IS_CHANGE = new ErrorCode(1009002008, "流程模型发生改变");

    // ========== 流程定义 1009003000 ==========
    ErrorCode PROCESS_DEFINITION_KEY_NOT_MATCH = new ErrorCode(1009003000, "流程定义的标识期望是({})，当前是({})，请修改 BPMN 流程图");
    ErrorCode PROCESS_DEFINITION_NAME_NOT_MATCH = new ErrorCode(1009003001, "流程定义的名字期望是({})，当前是({})，请修改 BPMN 流程图");
    ErrorCode PROCESS_DEFINITION_NOT_EXISTS = new ErrorCode(1009003002, "流程定义不存在");
    ErrorCode PROCESS_DEFINITION_IS_SUSPENDED = new ErrorCode(1009003003, "当前流程已被修改，请到发起流程页面重新提交");
    ErrorCode PROCESS_DEFINITION_BPMN_MODEL_NOT_EXISTS = new ErrorCode(1009003004, "流程定义的模型不存在");

    // ========== 流程实例 1009004000 ==========
    ErrorCode PROCESS_INSTANCE_NOT_EXISTS = new ErrorCode(1009004000, "流程实例不存在");
    ErrorCode PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS = new ErrorCode(1009004001, "流程取消失败，流程不处于运行中");
    ErrorCode PROCESS_INSTANCE_CANCEL_FAIL_NOT_SELF = new ErrorCode(1009004002, "流程取消失败，该流程不是你发起的");
    ErrorCode PROCESS_USER_TASK_NOT_SELF = new ErrorCode(1009004003, "任务处理人不能为空");
    ErrorCode PROCESS_INSTANCE_RETRACT_FAIL_NOT_SELF = new ErrorCode(1009004004, "流程撤回失败，该流程不是你发起的");
    ErrorCode PROCESS_INSTANCE_RETRACT_FAIL_IS_END = new ErrorCode(1009004005, "流程撤回失败，该流程已经被处理");

    // ========== 流程任务 1009005000 ==========
    ErrorCode TASK_COMPLETE_FAIL_NOT_EXISTS = new ErrorCode(1009005000, "审批任务失败，原因：该任务不处于未审批");
    ErrorCode TASK_COMPLETE_FAIL_ASSIGN_NOT_SELF = new ErrorCode(1009005001, "审批任务失败，原因：该任务的审批人不是你");
    ErrorCode TASK_NOT_EXIST = new ErrorCode(1009005002, "流程任务不存在");
    ErrorCode TASK_NOT_REVOCABLE = new ErrorCode(1009005003, "任务撤回失败，不满足任务撤回条件");


    // ========== 流程任务分配规则 1009006000 ==========
    ErrorCode TASK_ASSIGN_RULE_EXISTS = new ErrorCode(1009006000, "流程({}) 的任务({}) 已经存在分配规则");
    ErrorCode TASK_ASSIGN_RULE_NOT_EXISTS = new ErrorCode(1009006001, "流程任务分配规则不存在");
    ErrorCode TASK_UPDATE_FAIL_NOT_MODEL = new ErrorCode(1009006002, "只有流程模型的任务分配规则，才允许被修改");
    ErrorCode TASK_CREATE_FAIL_NO_CANDIDATE_USER = new ErrorCode(1009006003, "操作失败，原因：找不到任务的审批人！");
    ErrorCode TASK_ASSIGN_SCRIPT_NOT_EXISTS = new ErrorCode(1009006004, "操作失败，原因：任务分配脚本({}) 不存在");

    // ========== 动态表单模块 1009010000 ==========
    ErrorCode FORM_NOT_EXISTS = new ErrorCode(1009010000, "动态表单不存在");
    ErrorCode FORM_IS_EXISTS = new ErrorCode(1009010002, "动态表单已存在");
    ErrorCode FORM_FIELD_REPEAT = new ErrorCode(1009010001, "表单项({}) 和 ({}) 使用了相同的字段名({})");
    ErrorCode FORM_IS_USED = new ErrorCode(1009010003, "该表单已被使用，不能删除");

    // ========== 用户组模块 1009011000 ==========
    ErrorCode USER_GROUP_NOT_EXISTS = new ErrorCode(1009011000, "用户组不存在");
    ErrorCode USER_GROUP_IS_DISABLE = new ErrorCode(1009011001, "名字为【{}】的用户组已被禁用");


    ErrorCode APPROVAL_PROCESS_NOT_EXISTS = new ErrorCode(1009020000, "项目立项不存在");

    ErrorCode CORRECTION_NOT_EXISTS = new ErrorCode(1009030000, "补卡流程不存在");

    ErrorCode BUDGET_APPLICATION_NOT_EXISTS = new ErrorCode(1009040000, "资金预算申请不存在");
    ErrorCode ORDER_IS_ERROR = new ErrorCode(1009040001, "排序参数有误");

    // ========== 草稿箱模块 1009050000 ==========
    ErrorCode SAVE_PROCESS_INSTANCE_NOT_EXIST = new ErrorCode(1009050001, "该任务不存在");
    ErrorCode SAVE_PROCESS_INSTANCE_DELETE_NOT_AUTHORIZED = new ErrorCode(1009050002, "无权限删除该任务");
    ErrorCode SAVE_PROCESS_FILE_UPLOAD_ERROR = new ErrorCode(1009050003, "表单文件部分发生改变，请重新上传");


}
