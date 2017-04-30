package bing.system.dao;

import java.util.List;

import bing.system.condition.SysResourceCondition;
import bing.system.model.SysResource;
import bing.system.vo.SysResourceVO;

public interface SysResourceDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysResource entity);

	int insertSelective(SysResource entity);

	SysResource selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysResource entity);

	int updateByPrimaryKey(SysResource entity);

	SysResource getByURL(String url);

	List<SysResourceVO> listByCondition(SysResourceCondition condition);

}