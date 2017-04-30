package bing.system.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import bing.constant.HiddenEnum;
import bing.domain.GenericObject;

public class SysRole extends GenericObject implements Serializable {

	private static final long serialVersionUID = 745422160006062721L;

	private Integer id;

	@NotBlank(message = "{code.required}")
	private String code;

	@NotBlank(message = "{name.required}")
	private String name;

	private String remark;

	private Integer hidden = HiddenEnum.HIDDEN.ordinal();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Integer getHidden() {
		return hidden;
	}

	public void setHidden(Integer hidden) {
		this.hidden = hidden;
	}

}