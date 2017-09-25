package bing.domain;

import bing.constant.GlobalConstants;
import bing.constant.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

@NoArgsConstructor
public class GenericVO extends GenericObject {

    protected static final long serialVersionUID = -6547881868397315449L;

    @ApiModelProperty(value = "主键", dataType = "Integer", example = "1")
    protected Integer id;

    @ApiModelProperty(value = "状态文字", dataType = "String", example = "正常")
    protected String statusText;

    @ApiModelProperty(value = "创建时间", dataType = "String", example = "2017-07-07 12:00:00")
    protected String createDateTime;

    @ApiModelProperty(value = "更新时间", dataType = "String", example = "2017-07-07 12:00:00")
    protected String updateDateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusText() {
        if (StatusEnum.NORMAL.ordinal() == status) {
            statusText = "正常";
        } else if (StatusEnum.LOCKED.ordinal() == status) {
            statusText = "锁定";
        } else if (StatusEnum.DELETED.ordinal() == status) {
            statusText = "删除";
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public String getCreateUser() {
        return createUser;
    }

    @Override
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Override
    public Date getCreateDate() {
        if (createDate != null) {
            return (Date) createDate.clone();
        } else {
            return null;
        }
    }

    @Override
    public void setCreateDate(Date createDate) {
        if (createDate != null) {
            this.createDate = (Date) createDate.clone();
        } else {
            this.createDate = null;
        }
    }

    public String getCreateDateTime() {
        if (createDate != null && StringUtils.isBlank(createDateTime)) {
            createDateTime = DateFormatUtils.format(createDate, GlobalConstants.DATE_TIME_FORMAT);
        }
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    @Override
    public String getUpdateUser() {
        return updateUser;
    }

    @Override
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public Date getUpdateDate() {
        if (updateDate != null) {
            return (Date) updateDate.clone();
        } else {
            return null;
        }
    }

    @Override
    public void setUpdateDate(Date updateDate) {
        if (updateDate != null) {
            this.updateDate = (Date) updateDate.clone();
        } else {
            this.updateDate = null;
        }
    }

    public String getUpdateDateTime() {
        if (updateDate != null && StringUtils.isBlank(updateDateTime)) {
            updateDateTime = DateFormatUtils.format(updateDate, GlobalConstants.DATE_TIME_FORMAT);
        }
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
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
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GenericVO other = (GenericVO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
