package bing.system.condition;

import bing.domain.GenericCondition;

public class SysResourceCondition extends GenericCondition {

	private String name;

	private Integer type;

	private Integer categoryId;

	public SysResourceCondition() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

}
