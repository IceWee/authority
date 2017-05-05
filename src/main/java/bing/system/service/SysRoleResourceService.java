package bing.system.service;

import bing.domain.GenericService;
import bing.system.condition.SysRoleResourceCondition;
import bing.system.model.SysRoleResource;

public interface SysRoleResourceService extends GenericService<SysRoleResource, SysRoleResource, SysRoleResourceCondition> {

	/**
	 * 保存角色资源关联关系
	 * 
	 * @param roleId
	 * @param resourceIds
	 * @param username
	 */
	void saveRoleResources(Integer roleId, Integer[] resourceIds, String username);

}
