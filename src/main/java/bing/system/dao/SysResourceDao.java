package bing.system.dao;

import java.util.List;

import bing.system.condition.SysResourceCondition;
import bing.system.model.SysResource;
import bing.system.vo.SysResourceVO;
import bing.system.vo.URIRoleId;

public interface SysResourceDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysResource entity);

	int insertSelective(SysResource entity);

	SysResource selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysResource entity);

	int updateByPrimaryKey(SysResource entity);

	SysResource getByURL(String url);

	List<SysResourceVO> listByCondition(SysResourceCondition condition);

	int countByCategoryId(Integer categoryId);

	List<SysResource> listByRoleId(Integer roleId);

	List<SysResource> listAll();

	/**
	 * 获取用户具备的资源ID列表
	 * 
	 * @param userId
	 * @return
	 */
	List<Integer> listResourceIdByUserId(Integer userId);

	/**
	 * 获取系统中全部的URL和角色ID关系
	 * 
	 * @return
	 */
	List<URIRoleId> listAllURIRoleId();

}