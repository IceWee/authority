package bing.system.condition;

import bing.domain.GenericCondition;

public class SysRoleCondition extends GenericCondition {

	private String code;
	private String name;
	private Long status;

	public SysRoleCondition() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

}
