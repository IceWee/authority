package bing.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import bing.domain.GenericPage;
import bing.model.SysUser;

public interface SysUserService extends UserDetailsService {

	GenericPage<SysUser> listByPage(int pageNo, int pageSize, String name);

}
