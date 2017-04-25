package bing.conditions;

import bing.domain.GenericCondition;

/**
 * 用户查询条件
 * 
 * @author IceWee
 */
public class SysUserCondition extends GenericCondition {

	private String username;
	private String name;
	private String mobile;
	private Long status;

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

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

}
