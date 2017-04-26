package bing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.conditions.SysUserCondition;
import bing.dao.SysUserDao;
import bing.domain.GenericPage;
import bing.model.SysUser;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser userDetails = sysUserDao.getByUsername(username);
		if (userDetails == null) {
			throw new UsernameNotFoundException("User " + username + " has no GrantedAuthority");
		}
		// TODO 后续需要根据用户查询出全部权限
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ADMIN"));
		userDetails.setAuthorities(authorities);
		return userDetails;
	}

	@Override
	public GenericPage<SysUser> listByPage(SysUserCondition sysUserCondition) {
		Long pageNo = sysUserCondition.getPageNo();
		PageHelper.startPage(pageNo.intValue(), sysUserCondition.getPageSize().intValue());
		List<SysUser> list = sysUserDao.listByCondition(sysUserCondition);
		PageInfo<SysUser> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNo, pageInfo.getTotal(), list);
	}

	@Override
	public void saveOrUpdate(SysUser sysUser) {

	}

	@Override
	public SysUser get(Integer id) {
		return sysUserDao.selectByPrimaryKey(id);
	}

}
