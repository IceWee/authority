package bing.system.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import bing.security.URISecurityConfigs;

/**
 * 权限服务接口
 * 
 * @author IceWee
 */
public interface AuthorizeService extends UserDetailsService {

	/**
	 * 获取系统中全部的资源地址和访问该地址应具备的角色ID列表
	 * 
	 * @return
	 */
	List<URISecurityConfigs> listURISecurityConfigs();

}
