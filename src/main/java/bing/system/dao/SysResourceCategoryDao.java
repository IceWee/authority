package bing.system.dao;

import java.util.List;

import bing.system.model.SysResourceCategory;
import bing.system.vo.SysResourceCategoryVO;

public interface SysResourceCategoryDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysResourceCategory entity);

	int insertSelective(SysResourceCategory entity);

	SysResourceCategory selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysResourceCategory entity);

	int updateByPrimaryKey(SysResourceCategory entity);

	List<SysResourceCategoryVO> listByParentId(Integer parentId);

	List<SysResourceCategoryVO> listAll();

}