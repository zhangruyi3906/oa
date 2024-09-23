package com.lh.oa.module.system.full.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class InfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Collection<?> list;

	private int pageNo;

	private int pageSize;

	private int total;

	public InfoEntity() {
	}

	public InfoEntity(int pageNo, int pageSize, int total, Collection<?> list) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.total = total;
		this.list = list;
	}

	public InfoEntity(int total, Collection<?> list) {
		this(0, 0, total, list);
	}


	public InfoEntity data(Collection<?> records) {
		this.list = (records == null || records.size() == 0) ? null : records;
		return this;
	}

	public InfoEntity total(int total) {
		this.total = total;
		return this;
	}
	
}
