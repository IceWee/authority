package bing.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bing.security.URISecurityConfigs;
import bing.system.dao.SysResourceDao;
import bing.system.dao.SysRoleDao;
import bing.system.dao.SysUserDao;
import bing.system.model.SysRole;
import bing.system.model.SysUser;
import bing.system.vo.URIRoleId;

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
	private SysResourceDao sysResourceDao;

	@Override
	// @Cacheable(cacheNames = {EhCacheNames.USER_CACHE})
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser userDetails = sysUserDao.getByUsername(username);
		if (userDetails == null) {
			throw new UsernameNotFoundException("User " + username + " has no GrantedAuthority");
		}
		// 将当前登录用户具备的角色ID全部取出放在UserDetails中供后续访问资源时验证使用
		List<SysRole> ownRoles = sysRoleDao.listByUserId(userDetails.getId());
		List<Integer> ownRoleIds = ownRoles.stream().map(role -> role.getId()).collect(Collectors.toList());
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		ownRoleIds.forEach(roleId -> {
			authorities.add(new SimpleGrantedAuthority(Objects.toString(roleId)));
		});
		userDetails.setAuthorities(authorities);
		return userDetails;
	}

	@Override
	public List<URISecurityConfigs> listURISecurityConfigs() {
		List<URISecurityConfigs> uriSecurityConfigs = new ArrayList<>();
		// uri - roleId 列表，多对多
		List<URIRoleId> uriRoleIds = sysResourceDao.listAllURIRoleId();
		// distinct uri， 产生不重复的URI
		List<String> uris = uriRoleIds.stream().map(bean -> bean.getUri()).distinct().collect(Collectors.toList());
		// 遍历URI构造返回类型并添加到返回集合中
		uris.forEach(uri -> {
			uriSecurityConfigs.add(new URISecurityConfigs(uri));
		});
		// 遍历返回集合，将URI匹配的角色ID添加进来
		uriSecurityConfigs.forEach(uriConfigs -> {
			uriRoleIds.forEach(uriRoleId -> {
				if (StringUtils.equals(uriConfigs.getUri(), uriRoleId.getUri())) {
					uriConfigs.addConfig(uriRoleId.getRoleId());
				}
			});
		});
		return uriSecurityConfigs;
	}

}
