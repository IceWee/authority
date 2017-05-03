package bing.system.vo;

import java.util.ArrayList;
import java.util.List;

import bing.system.model.SysUser;

/**
 * 角色用户值对象
 * 
 * @author IceWee
 */
public class RoleUserVO {

	private Integer roleId;

	private List<SysUser> unselectUsers = new ArrayList<>();

	private List<SysUser> selectedUsers = new ArrayList<>();

	public RoleUserVO() {
		super();
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public List<SysUser> getUnselectUsers() {
		return unselectUsers;
	}

	public void setUnselectUsers(List<SysUser> unselectUsers) {
		this.unselectUsers = unselectUsers;
	}

	public List<SysUser> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<SysUser> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

}
