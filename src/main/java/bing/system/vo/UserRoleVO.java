package bing.system.vo;

import java.util.ArrayList;
import java.util.List;

import bing.system.model.SysRole;

/**
 * 用户角色值对象
 * 
 * @author IceWee
 */
public class UserRoleVO {

	private Integer userId;

	private List<SysRole> unselectRoles = new ArrayList<>();

	private List<SysRole> selectedRoles = new ArrayList<>();

	public UserRoleVO() {
		super();
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<SysRole> getUnselectRoles() {
		return unselectRoles;
	}

	public void setUnselectRoles(List<SysRole> unselectRoles) {
		this.unselectRoles = unselectRoles;
	}

	public List<SysRole> getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(List<SysRole> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

}
