package bing.system.dao;

import bing.system.model.SysRoleResource;

public interface SysRoleResourceDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysRoleResource record);

	int insertSelective(SysRoleResource record);

	SysRoleResource selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysRoleResource record);

	int updateByPrimaryKey(SysRoleResource record);

}