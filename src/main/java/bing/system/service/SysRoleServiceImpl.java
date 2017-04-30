package bing.system.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bing.constant.StatusEnum;
import bing.domain.GenericPage;
import bing.exception.BusinessException;
import bing.system.condition.SysRoleCondition;
import bing.system.dao.SysRoleDao;
import bing.system.exception.RoleExceptionCodes;
import bing.system.model.SysRole;
import bing.system.vo.SysRoleVO;

@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;

	@Override
	public GenericPage<SysRoleVO> listByPage(SysRoleCondition condition) {
		Long pageNo = condition.getPageNo();
		PageHelper.startPage(pageNo.intValue(), condition.getPageSize().intValue());
		List<SysRoleVO> list = sysRoleDao.listByCondition(condition);
		PageInfo<SysRoleVO> pageInfo = new PageInfo<>(list);
		return new GenericPage<>(pageNo, pageInfo.getTotal(), list);
	}

	@Override
	public void save(SysRole entity) {
		entity.setId(null);
		SysRole persistRole = sysRoleDao.getByCode(entity.getCode());
		if (persistRole != null) {
			throw new BusinessException(RoleExceptionCodes.CODE_USED);
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
			throw new BusinessException(RoleExceptionCodes.CODE_FORBIDDEN_MODIFY);
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
		SysRole entity = new SysRole();
		entity.setId(id);
		entity.setStatus(StatusEnum.DELETED.ordinal());
		entity.setUpdateUser(username);
		entity.setUpdateDate(new Date());
		sysRoleDao.updateByPrimaryKeySelective(entity);
	}

}
