package bing.system.dao;

import bing.system.model.SysMenu;

public interface SysMenuDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysMenu record);

	int insertSelective(SysMenu record);

	SysMenu selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysMenu record);

	int updateByPrimaryKey(SysMenu record);

}