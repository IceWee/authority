package bing.system.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.constant.StatusEnum;
import bing.domain.GenericPage;
import bing.exception.BusinessException;
import bing.system.condition.SysRoleCondition;
import bing.system.dao.SysRoleDao;
import bing.system.dao.SysUserRoleDao;
import bing.system.exception.RoleExceptionCodes;
import bing.system.model.SysRole;
import bing.system.model.SysUserRole;
import bing.system.vo.SysRoleVO;
import bing.system.vo.UserRoleVO;

@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;

	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Override
	public GenericPage<SysRoleVO> listByPage(SysRoleCondition condition) {
		int pageNumber = condition.getPageNumber();
		int pageSize = condition.getPageSize();
		PageHelper.startPage(pageNumber, condition.getPageSize());
		List<SysRoleVO> list = sysRoleDao.listByCondition(condition);
		PageInfo<SysRoleVO> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNumber, pageSize, pageInfo.getTotal(), list);
	}

	@Override
	public void save(SysRole entity) {
		entity.setId(null);
		SysRole persistRole = sysRoleDao.getByCode(entity.getCode());
		if (persistRole != null) {
			throw new BusinessException(RoleExceptionCodes.singleton().CODE_USED);
		}
		entity.setCreateDate(new Date());
		entity.setUpdateDate(new Date());
		sysRoleDao.insert(entity);
	}

	@Override
	public void update(SysRole entity) {
		SysRole persistRole = sysRoleDao.selectByPrimaryKey(entity.getId());
		String origionCode = persistRole.getCode();
		String newCode = entity.getCode();
		if (!StringUtils.equals(origionCode, newCode)) {
			throw new BusinessException(RoleExceptionCodes.singleton().CODE_FORBIDDEN_MODIFY);
		}
		entity.setCreateDate(new Date());
		entity.setUpdateDate(new Date());
		sysRoleDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public SysRole getById(Integer id) {
		return sysRoleDao.selectByPrimaryKey(id);
	}

	@Override
	public void deleteById(Integer id, String username) {
		int count = sysUserRoleDao.countByRoleId(id);
		if (count > 0) {
			throw new BusinessException(RoleExceptionCodes.singleton().AUTHORIZED_TO_USER);
		}
		SysRole entity = new SysRole();
		entity.setId(id);
		entity.setStatus(StatusEnum.DELETED.ordinal());
		entity.setUpdateUser(username);
		entity.setUpdateDate(new Date());
		sysRoleDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public UserRoleVO getUserRoles(Integer userId) {
		List<SysRole> roles = sysRoleDao.listAll();
		List<SysRole> selectedRoles = sysRoleDao.listByUserId(userId);
		List<Integer> selectedRoleIds = selectedRoles.stream().map(selectedRole -> selectedRole.getId()).collect(Collectors.toList());
		List<SysRole> unselectRoles = roles.stream().filter(role -> !selectedRoleIds.contains(role.getId())).collect(Collectors.toList());
		UserRoleVO userRoles = new UserRoleVO();
		userRoles.setUserId(userId);
		userRoles.setUnselectRoles(unselectRoles);
		userRoles.setSelectedRoles(selectedRoles);
		return userRoles;
	}

	@Override
	@Transactional
	public void saveUserRoles(Integer userId, Integer[] roleIds, String username) {
		sysUserRoleDao.deleteByUserId(userId);
		if (ArrayUtils.isNotEmpty(roleIds)) {
			List<SysUserRole> entities = Arrays.asList(roleIds).stream().map(roleId -> createUserRole(userId, roleId, username))
					.collect(Collectors.toList());
			sysUserRoleDao.insertBatch(entities);
		}
	}

	@Override
	public List<SysRole> listAll() {
		return sysRoleDao.listAll();
	}

	private SysUserRole createUserRole(Integer userId, Integer roleId, String usernmae) {
		SysUserRole entity = new SysUserRole(userId, roleId);
		Date now = new Date();
		entity.setCreateDate(now);
		entity.setCreateUser(usernmae);
		entity.setUpdateDate(now);
		entity.setUpdateUser(usernmae);
		return entity;
	}

}
