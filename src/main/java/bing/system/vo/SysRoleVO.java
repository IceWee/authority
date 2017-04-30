package bing.system.vo;

import bing.domain.GenericVO;

public class SysRoleVO extends GenericVO {

	private static final long serialVersionUID = 7276604397939483725L;

	private String code;

	private String name;

	private String remark;

	public SysRoleVO() {
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
