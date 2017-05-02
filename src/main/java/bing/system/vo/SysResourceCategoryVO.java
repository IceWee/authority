package bing.system.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import bing.domain.GenericVO;

public class SysResourceCategoryVO extends GenericVO {

	private static final long serialVersionUID = -4770793175779247821L;

	private Integer parentId;

	@JsonProperty("text")
	private String name;

	private String remark;

	private List<SysResourceCategoryVO> children = new ArrayList<>();

	public SysResourceCategoryVO() {
		super();
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SysResourceCategoryVO> getChildren() {
		return children;
	}

	public void setChildren(List<SysResourceCategoryVO> children) {
		this.children = children;
	}

	public void addChild(SysResourceCategoryVO child) {
		children.add(child);
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}