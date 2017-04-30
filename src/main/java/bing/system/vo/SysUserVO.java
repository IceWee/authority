package bing.system.vo;

import bing.domain.GenericVO;

public class SysUserVO extends GenericVO {

	private static final long serialVersionUID = -7983749899316231902L;

	private String username;

	private String name;

	private String mobile;

	public SysUserVO() {
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
