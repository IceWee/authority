package bing.system.service;

import bing.domain.GenericService;
import bing.system.condition.SysUserCondition;
import bing.system.model.SysUser;
import bing.system.vo.RoleUserVO;
import bing.system.vo.SysUserVO;

public interface SysUserService extends GenericService<SysUser, SysUserVO, SysUserCondition> {

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
	 * @param username
	 */
	void saveRoleUsers(Integer roleId, Integer[] userIds, String username);

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 */
	void changePassword(Integer userId, String oldPassword, String newPassword);

	/**
	 * 更新用户信息及角色
	 * 
	 * @param entity
	 */
	void updateWithRole(SysUser entity);

	/**
	 * 锁定用户
	 * 
	 * @param id
	 * @param username
	 */
	void lockById(Integer id, String username);

	/**
	 * 解除锁定用户
	 * 
	 * @param id
	 * @param username
	 */
	void unlockById(Integer id, String username);

}
