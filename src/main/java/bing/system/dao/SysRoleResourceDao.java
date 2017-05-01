package bing.system.dao;

import java.util.List;

import bing.system.condition.SysRoleResourceCondition;
import bing.system.model.SysRoleResource;

public interface SysRoleResourceDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysRoleResource entity);

	int insertSelective(SysRoleResource entity);

	SysRoleResource selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysRoleResource entity);

	int updateByPrimaryKey(SysRoleResource entity);

	List<SysRoleResource> listByCondition(SysRoleResourceCondition condition);

}