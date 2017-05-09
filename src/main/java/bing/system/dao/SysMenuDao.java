package bing.system.dao;

import java.util.List;

import bing.system.condition.SysMenuCondition;
import bing.system.model.SysMenu;
import bing.system.vo.SysMenuVO;

public interface SysMenuDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysMenu entity);

	int insertSelective(SysMenu entity);

	SysMenu selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysMenu entity);

	int updateByPrimaryKey(SysMenu entity);

	List<SysMenuVO> listByCondition(SysMenuCondition condition);

	List<SysMenuVO> listByParentId(Integer parentId);

	List<SysMenuVO> listAll();

	int countByParentId(Integer parentId);

	int countByResourceId(Integer resourceId);

}