package com.lh.oa.module.system.dal.dataobject.baseRegion;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 区域信息表
 * </p>
 *
 * @author ${author}
 * @since 2023-09-13
 */
@Data
@TableName("base_region")
public class BaseRegion extends Model<BaseRegion> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private List<BaseRegion> children = new ArrayList<>();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMergerName() {
        return mergerName;
    }

    public void setMergerName(String mergerName) {
        this.mergerName = mergerName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BaseRegion{" +
        ", id=" + id +
        ", parentId=" + parentId +
        ", shortName=" + shortName +
        ", name=" + name +
        ", mergerName=" + mergerName +
        ", level=" + level +
        ", pinyin=" + pinyin +
        ", code=" + code +
        ", zipCode=" + zipCode +
        ", first=" + first +
        ", lng=" + lng +
        ", lat=" + lat +
        "}";
    }
}
