package bing.system.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import bing.domain.GenericObject;

public class SysResource extends GenericObject implements Serializable {

	private static final long serialVersionUID = -6827672328719969042L;

	private Integer id;

	@NotNull(message = "{name.required}")
	private String name;

	@NotNull(message = "{category.required}")
	private Integer categoryId;

	private Integer type;

	@NotNull(message = "{url.required}")
	private String url;

	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

}