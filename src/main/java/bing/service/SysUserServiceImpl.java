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

import bing.dao.SysUserDao;
import bing.domain.GenericPage;
import bing.model.SysUser;

@Service("sysUserService")  
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser sysUser = sysUserDao.getByUsername(username);
		if (sysUser == null) {
			throw new UsernameNotFoundException("User " + username + " has no GrantedAuthority");  
		}
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ADMIN"));
		sysUser.setAuthorities(authorities);
		return sysUser;
	}

	@Override
	public GenericPage<SysUser> listByPage(int pageNo, int pageSize, String name) {
		PageHelper.startPage(pageNo, pageSize);
		List<SysUser> list = sysUserDao.listByName(name);
		PageInfo<SysUser> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNo, pageInfo.getTotal(), list);
	}

}
