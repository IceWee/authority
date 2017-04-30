package bing.system.vo;

import bing.domain.GenericVO;

public class SysRoleVO extends GenericVO {

	private static final long serialVersionUID = 7276604397939483725L;

	private String code;

	private String mobile;

	public SysRoleVO() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
