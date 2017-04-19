package bing.model;

import bing.domain.BaseUser;

public class SysUser extends BaseUser {

	private static final long serialVersionUID = 5217735149888451989L;

	public SysUser() {
		super();
	}

	public SysUser(String username, String password) {
		super(username, password);
	}

}
