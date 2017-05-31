package bing.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bing.constant.GlobalConstants;
import bing.security.URISecurityConfigs;
import bing.system.dao.SysRoleDao;
import bing.system.dao.SysRoleResourceDao;
import bing.system.dao.SysUserDao;
import bing.system.model.SysRole;
import bing.system.model.SysUser;
import bing.system.vo.URIRole;

/**
 * 权限服务
 * 
 * @author IceWee
 */
@Service("authorizeService")
public class AuthorizeServiceImpl implements AuthorizeService {

	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	private SysRoleDao sysRoleDao;

	@Autowired
	private SysRoleResourceDao sysRoleResourceDao;

	@Override
	// @Cacheable(cacheNames = {EhCacheNames.USER_CACHE})
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser userDetails = sysUserDao.getByUsername(username);
		if (userDetails == null) {
			throw new UsernameNotFoundException("User " + username + " has no GrantedAuthority");
		}
		// 将当前登录用户具备的角色编码全部取出放在UserDetails中供后续访问资源时验证使用
		List<SysRole> ownRoles = sysRoleDao.listByUserId(userDetails.getId());
		List<String> ownRoleCodes = ownRoles.stream().map(role -> role.getCode()).collect(Collectors.toList());
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		ownRoleCodes.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role));
		});
		userDetails.setAuthorities(authorities);
		return userDetails;
	}

	@Override
	public List<URISecurityConfigs> listURISecurityConfigs() {
		List<URISecurityConfigs> uriSecurityConfigs = new ArrayList<>();
		// uri - 角色code 列表，多对多
		List<URIRole> uriRoles = sysRoleResourceDao.listAllURIRole();
		// distinct uri， 产生不重复的URI
		List<String> uris = uriRoles.stream().map(bean -> bean.getUri()).distinct().collect(Collectors.toList());
		// 遍历URI构造返回类型并添加到返回集合中
		uris.forEach(uri -> {
			uriSecurityConfigs.add(new URISecurityConfigs(uri));
		});
		// 遍历返回集合，将URI匹配的角色ID添加进来
		uriSecurityConfigs.forEach(uriConfigs -> {
			uriRoles.forEach(uriRole -> {
				if (StringUtils.equals(uriConfigs.getUri(), uriRole.getUri())) {
					if (StringUtils.isBlank(uriRole.getRoleCode())) { // 如果当前资源未配置可访问角色默认为admin
						uriConfigs.addConfig(GlobalConstants.ADMIN);
					} else {
						uriConfigs.addConfig(uriRole.getRoleCode());
					}
				}
			});
		});
		return uriSecurityConfigs;
	}

}
