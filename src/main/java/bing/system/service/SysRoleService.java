package bing.system.service;

import bing.domain.GenericService;
import bing.system.condition.SysRoleCondition;
import bing.system.model.SysRole;
import bing.system.vo.RoleUserVO;
import bing.system.vo.SysRoleVO;

public interface SysRoleService extends GenericService<SysRole, SysRoleVO, SysRoleCondition> {

	/**
	 * 获取角色用户对象
	 * 
	 * @param roleId
	 * @return
	 */
	RoleUserVO getRoleUsers(Integer roleId);

	/**
	 * 保存角色用户
	 * 
	 * @param roleId
	 * @param userIds
	 */
	void saveRoleUsers(Integer roleId, Integer[] userIds);

}
