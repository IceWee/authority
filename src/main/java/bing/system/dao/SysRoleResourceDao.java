package bing.system.dao;

import java.util.List;

import bing.system.condition.SysRoleResourceCondition;
import bing.system.model.SysRoleResource;
import bing.system.vo.URIRole;

public interface SysRoleResourceDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysRoleResource entity);

	int insertSelective(SysRoleResource entity);

	SysRoleResource selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysRoleResource entity);

	int updateByPrimaryKey(SysRoleResource entity);

	List<SysRoleResource> listByCondition(SysRoleResourceCondition condition);

	void deleteByRoleId(Integer roleId);

	int insertBatch(List<SysRoleResource> entities);

	int countByResourceId(Integer resourceId);

	/**
	 * 获取系统中全部的URL和角色ID关系
	 * 
	 * @return
	 */
	List<URIRole> listAllURIRole();

}