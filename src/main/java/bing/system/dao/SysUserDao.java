package bing.system.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import bing.constant.EhCacheNames;
import bing.system.condition.SysUserCondition;
import bing.system.model.SysUser;
import bing.system.vo.SysUserVO;

public interface SysUserDao {

	int deleteByPrimaryKey(Integer id);

	int insert(SysUser entity);

	int insertSelective(SysUser entity);

	SysUser selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysUser entity);

	int updateByPrimaryKey(SysUser entity);

	@Cacheable(value = EhCacheNames.USER_CACHE)
	SysUser getByUsername(String username);

	List<SysUserVO> listByCondition(SysUserCondition condition);

}