package bing.system.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		Long pageNo = condition.getPageNo();
		PageHelper.startPage(pageNo.intValue(), condition.getPageSize().intValue());
		List<SysRoleResource> list = sysRoleResourceDao.listByCondition(condition);
		PageInfo<SysRoleResource> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNo, pageInfo.getTotal(), list);
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

}
