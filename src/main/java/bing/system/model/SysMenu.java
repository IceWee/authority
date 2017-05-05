package bing.system.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import bing.domain.GenericObject;

public class SysMenu extends GenericObject implements Serializable {

	private static final long serialVersionUID = -245722492846076538L;

	private Integer id;

	@NotNull(message = "{name.required}")
	private String name;

	@NotNull(message = "{menu.parent.required}")
	private Integer parentId;

	private Integer resourceId;

	private Integer sort = 1;

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

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

}