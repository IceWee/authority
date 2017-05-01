package bing.system.vo;

import bing.domain.GenericVO;
import bing.system.constant.ResourceTypeEnum;

public class SysResourceVO extends GenericVO {

	private static final long serialVersionUID = 8609364052278928141L;

	private String name;

	private Integer type;

	private String typeText;

	private String url;

	private Integer categoryId;

	private String remark;

	public SysResourceVO() {
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

	public String getTypeText() {
		if (type != null) {
			if (type == ResourceTypeEnum.FUNCTION.ordinal()) {
				typeText = "一般功能";
			} else if (type == ResourceTypeEnum.FUNCTION.ordinal()) {
				typeText = "数据接口";
			} else {
				typeText = "其他";
			}
		}
		return typeText;
	}

	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
