package bing.system.condition;

import bing.domain.GenericCondition;

public class SysUserCondition extends GenericCondition {

	private String username;

	private String name;

	private String mobile;

	public SysUserCondition() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
