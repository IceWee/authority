package bing.system.dao;

import java.util.List;

import bing.system.model.SysUserRole;

public interface SysUserRoleDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysUserRole eneity);

	int insertSelective(SysUserRole eneity);

	SysUserRole selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysUserRole eneity);

	int updateByPrimaryKey(SysUserRole eneity);

	void deleteByUserId(Integer userId);

	void deleteByRoleId(Integer roleId);

	int insertBatch(List<SysUserRole> entities);

	int countByRoleId(Integer roleId);

}