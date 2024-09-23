package com.lh.oa.module.system.full.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class KeyValue implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String code;

	private String name;
}
