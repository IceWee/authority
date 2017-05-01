package bing.system.condition;

import bing.domain.GenericCondition;

public class SysRoleResourceCondition extends GenericCondition {

	private Integer roleId;

	private Integer resourceId;

	public SysRoleResourceCondition() {
		super();
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
