package bing.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.constant.StatusEnum;
import bing.domain.GenericPage;
import bing.exception.BusinessException;
import bing.system.condition.SysUserCondition;
import bing.system.dao.SysUserDao;
import bing.system.exception.UserExceptionCodes;
import bing.system.model.SysUser;
import bing.system.vo.SysUserVO;
import bing.util.PasswordUtils;

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
	public GenericPage<SysUserVO> listByPage(SysUserCondition condition) {
		Long pageNo = condition.getPageNo();
		PageHelper.startPage(pageNo.intValue(), condition.getPageSize().intValue());
		List<SysUserVO> list = sysUserDao.listByCondition(condition);
		PageInfo<SysUserVO> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNo, pageInfo.getTotal(), list);
	}

	@Override
	public void save(SysUser entity) {
		entity.setId(null);
		SysUser persistUser = sysUserDao.getByUsername(entity.getUsername());
		if (persistUser != null) {
			throw new BusinessException(UserExceptionCodes.USERNAME_REGISTERED);
		}
		entity.setCreateDate(new Date());
		entity.setUpdateDate(new Date());
		// 密码加密保存
		String rawPassword = entity.getPassword();
		String encryptPasswd = PasswordUtils.encrypt(rawPassword);
		entity.setPassword(encryptPasswd);
		sysUserDao.insert(entity);
	}

	@Override
	public void update(SysUser entity) {
		SysUser persistUser = sysUserDao.selectByPrimaryKey(entity.getId());
		String origionUsername = persistUser.getUsername();
		String newUsername = entity.getUsername();
		if (!StringUtils.equals(origionUsername, newUsername)) {
			throw new BusinessException(UserExceptionCodes.USERNAME_FORBIDDEN_MODIFY);
		}
		entity.setCreateDate(new Date());
		entity.setUpdateDate(new Date());
		sysUserDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public SysUser getById(Integer id) {
		return sysUserDao.selectByPrimaryKey(id);
	}

	@Override
	public void deleteById(Integer id, String username) {
		SysUser entity = new SysUser();
		entity.setId(id);
		entity.setStatus(StatusEnum.DELETED.ordinal());
		entity.setUpdateUser(username);
		entity.setUpdateDate(new Date());
		sysUserDao.updateByPrimaryKeySelective(entity);
	}

}
