package bing.system.model;

import java.io.Serializable;

import bing.domain.GenericObject;

public class SysResourceCategory extends GenericObject implements Serializable {

	private static final long serialVersionUID = -335302522344473301L;

	private Integer id;

	private String name;

	private Integer parentId;

	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

}