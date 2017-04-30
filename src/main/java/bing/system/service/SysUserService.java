package bing.system.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import bing.domain.GenericService;
import bing.system.condition.SysUserCondition;
import bing.system.model.SysUser;
import bing.system.vo.SysUserVO;

public interface SysUserService extends UserDetailsService, GenericService<SysUser, SysUserVO, SysUserCondition> {

}
