package bing.system.model;

import java.io.Serializable;

import bing.domain.GenericObject;

public class SysRoleResource extends GenericObject implements Serializable {

	private static final long serialVersionUID = -395684170681026152L;

	private Integer id;

	private Integer roleId;

	private Integer resourceId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

}