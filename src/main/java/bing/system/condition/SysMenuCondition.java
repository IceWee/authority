package bing.system.condition;

import bing.domain.GenericCondition;

public class SysMenuCondition extends GenericCondition {

	private String name;

	private Integer parentId;

	public SysMenuCondition() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

}
