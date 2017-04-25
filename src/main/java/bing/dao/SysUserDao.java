package bing.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import bing.conditions.SysUserCondition;
import bing.constants.EhCacheNames;
import bing.model.SysUser;

@Cacheable
public interface SysUserDao {
	int deleteByPrimaryKey(Integer id);

	int insert(SysUser record);

	int insertSelective(SysUser record);

	SysUser selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysUser record);

	int updateByPrimaryKey(SysUser record);

	@Cacheable(value = EhCacheNames.USER_CACHE)
	SysUser getByUsername(String username);

	/**
	 * 用户列表
	 * 
	 * @param sysUserCondition
	 * @return
	 */
	List<SysUser> listByCondition(SysUserCondition sysUserCondition);
}