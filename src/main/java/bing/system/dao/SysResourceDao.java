package bing.system.dao;

import bing.system.model.SysResource;

public interface SysResourceDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysResource record);

	int insertSelective(SysResource record);

	SysResource selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysResource record);

	int updateByPrimaryKey(SysResource record);

}