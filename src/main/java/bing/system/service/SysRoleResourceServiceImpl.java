package bing.system.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.constant.StatusEnum;
import bing.domain.GenericPage;
import bing.system.condition.SysRoleResourceCondition;
import bing.system.dao.SysRoleResourceDao;
import bing.system.model.SysRoleResource;

@Service("sysRoleResourceService")
public class SysRoleResourceServiceImpl implements SysRoleResourceService {

	@Autowired
	private SysRoleResourceDao sysRoleResourceDao;

	@Override
	public GenericPage<SysRoleResource> listByPage(SysRoleResourceCondition condition) {
		Long pageNumber = condition.getPageNumber();
		PageHelper.startPage(pageNumber.intValue(), condition.getPageSize().intValue());
		List<SysRoleResource> list = sysRoleResourceDao.listByCondition(condition);
		PageInfo<SysRoleResource> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNumber, pageInfo.getTotal(), list);
	}

	@Override
	public void save(SysRoleResource entity) {
		entity.setCreateDate(new Date());
		entity.setUpdateDate(new Date());
		sysRoleResourceDao.insert(entity);
	}

	@Override
	public void update(SysRoleResource entity) {
		entity.setUpdateDate(new Date());
		sysRoleResourceDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public SysRoleResource getById(Integer id) {
		return sysRoleResourceDao.selectByPrimaryKey(id);
	}

	@Override
	public void deleteById(Integer id, String username) {
		SysRoleResource entity = new SysRoleResource();
		entity.setId(id);
		entity.setStatus(StatusEnum.DELETED.ordinal());
		entity.setUpdateUser(username);
		entity.setUpdateDate(new Date());
		sysRoleResourceDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	@Transactional
	public void saveRoleResources(Integer roleId, Integer[] resourceIds, String username) {
		sysRoleResourceDao.deleteByRoleId(roleId);
		if (ArrayUtils.isNotEmpty(resourceIds)) {
			List<SysRoleResource> entities = Arrays.asList(resourceIds).stream().map(resourceId -> createRoleResource(roleId, resourceId, username)).collect(Collectors.toList());
			sysRoleResourceDao.insertBatch(entities);
		}
	}

	private SysRoleResource createRoleResource(Integer roleId, Integer resourceId, String username) {
		SysRoleResource entity = new SysRoleResource(roleId, resourceId);
		Date now = new Date();
		entity.setCreateDate(now);
		entity.setCreateUser(username);
		entity.setUpdateDate(now);
		entity.setUpdateUser(username);
		return entity;
	}

}
