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
            } else if (type == ResourceTypeEnum.DATA_API.ordinal()) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SysResourceVO other = (SysResourceVO) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
