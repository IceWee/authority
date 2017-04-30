package bing.system.service;

import org.springframework.stereotype.Service;

import bing.domain.GenericPage;
import bing.system.condition.SysRoleCondition;
import bing.system.model.SysRole;

@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

	@Override
	public GenericPage<SysRole> listByPage(SysRoleCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(SysRole model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(SysRole model) {
		// TODO Auto-generated method stub

	}

	@Override
	public SysRole getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Integer id, String username) {
		// TODO Auto-generated method stub

	}

}
