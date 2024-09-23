package com.lh.oa.module.system.dal.dataobject.baseRegion;


import com.baomidou.mybatisplus.annotations.TableField;
import com.lh.oa.framework.common.pojo.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ${author}
 * @since 2023-09-13
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseRegionQuery extends PageParam {
    private Integer id;
    /**
     * 父id
     */
    @TableField("parent_id")
    private Integer parentId;
    /**
     * 简称
     */
    @TableField("short_name")
    private String shortName;
    /**
     * 名称
     */
    private String name;
    /**
     * 全称
     */
    @TableField("merger_name")
    private String mergerName;
    /**
     * 层级 1 2 3 省市区县
     */
    private Integer level;
    /**
     * 拼音
     */
    private String pinyin;
    /**
     * 长途区号
     */
    private String code;
    /**
     * 邮编
     */
    @TableField("zip_code")
    private String zipCode;
    /**
     * 首字母
     */
    private String first;
    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;
}