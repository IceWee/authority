package bing.system.dao;

import java.util.List;

import bing.system.condition.SysRoleCondition;
import bing.system.model.SysRole;
import bing.system.vo.SysRoleVO;

public interface SysRoleDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysRole entity);

	int insertSelective(SysRole entity);

	SysRole selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysRole entity);

	int updateByPrimaryKey(SysRole entity);

	SysRole getByCode(String code);

	List<SysRoleVO> listByCondition(SysRoleCondition condition);

	List<SysRole> listAll();

	List<SysRole> listByUserId(Integer userId);

}