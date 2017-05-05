package bing.system.dao;

import java.util.List;

import bing.system.condition.SysMenuCondition;
import bing.system.model.SysMenu;

public interface SysMenuDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysMenu entity);

	int insertSelective(SysMenu entity);

	SysMenu selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysMenu entity);

	int updateByPrimaryKey(SysMenu entity);

	List<SysMenu> listByCondition(SysMenuCondition condition);

	List<SysMenu> listByParentId(Integer parentId);

	List<SysMenu> listAll();

	int countByParentId(Integer parentId);

}