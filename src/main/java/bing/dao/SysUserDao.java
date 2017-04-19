package bing.dao;

import java.util.List;

import bing.model.SysUser;

public interface SysUserDao {
	int deleteByPrimaryKey(Integer id);

	int insert(SysUser record);

	int insertSelective(SysUser record);

	SysUser selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysUser record);

	int updateByPrimaryKey(SysUser record);

	SysUser getByUsername(String username);

	List<SysUser> listByName(String name);
}