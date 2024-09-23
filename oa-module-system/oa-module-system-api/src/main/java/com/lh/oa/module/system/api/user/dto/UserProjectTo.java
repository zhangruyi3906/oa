package com.lh.oa.module.system.api.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author tanghanlin
 * @since 2023/12/14
 */
@Getter
@Setter
@ToString
public class UserProjectTo implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

}
