package bing.system.model;

import java.io.Serializable;

import bing.domain.GenericObject;

public class SysUserRole extends GenericObject implements Serializable {

	private static final long serialVersionUID = -2183035361371500068L;

	private Integer id;

	private Integer userId;

	private Integer roleId;

	public SysUserRole() {
		super();
	}

	public SysUserRole(Integer userId, Integer roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}