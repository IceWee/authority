package bing.system.dao;

import java.util.List;

import bing.system.model.SysResourceCategory;

public interface SysResourceCategoryDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysResourceCategory entity);

	int insertSelective(SysResourceCategory entity);

	SysResourceCategory selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysResourceCategory entity);

	int updateByPrimaryKey(SysResourceCategory entity);

	List<SysResourceCategory> listByParentId(Integer parentId);

	List<SysResourceCategory> listAll();

	int countByParentId(Integer parentId);

}