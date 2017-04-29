package bing.system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.domain.GenericPage;
import bing.system.condition.SysUserCondition;
import bing.system.dao.SysUserDao;
import bing.system.model.SysUser;

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
	public GenericPage<SysUser> listUserByPage(SysUserCondition sysUserCondition) {
		Long pageNo = sysUserCondition.getPageNo();
		PageHelper.startPage(pageNo.intValue(), sysUserCondition.getPageSize().intValue());
		List<SysUser> list = sysUserDao.listByCondition(sysUserCondition);
		PageInfo<SysUser> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNo, pageInfo.getTotal(), list);
	}

	@Override
	public void saveUser(SysUser sysUser) {
		sysUser.setId(null);
		sysUserDao.insert(sysUser);
	}

	@Override
	public SysUser getUserById(Integer id) {
		return sysUserDao.selectByPrimaryKey(id);
	}

}
