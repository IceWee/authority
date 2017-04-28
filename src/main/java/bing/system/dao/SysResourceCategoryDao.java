package bing.system.dao;

import bing.system.model.SysResourceCategory;

public interface SysResourceCategoryDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysResourceCategory record);

	int insertSelective(SysResourceCategory record);

	SysResourceCategory selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysResourceCategory record);

	int updateByPrimaryKey(SysResourceCategory record);

}