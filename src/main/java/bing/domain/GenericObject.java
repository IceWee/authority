package bing.domain;

import bing.constant.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public abstract class GenericObject implements Serializable {

    private static final long serialVersionUID = -3001222131707495746L;

    @ApiModelProperty(value = "状态", dataType = "Integer", example = "0")
    protected Integer status;

    @ApiModelProperty(value = "创建人", dataType = "String", example = "超级管理员")
    protected String createUser;

    @ApiModelProperty(value = "创建日期", dataType = "String", example = "2017-07-07")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date createDate;

    @ApiModelProperty(value = "更新人", dataType = "String", example = "超级管理员")
    protected String updateUser;

    @ApiModelProperty(value = "更新日期", dataType = "String", example = "2017-07-07")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date updateDate;

    public Integer getStatus() {
        if (status == null) {
            status = StatusEnum.NORMAL.ordinal();
        }
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        if (createDate != null) {
            return (Date) createDate.clone();
        } else {
            return null;
        }
    }

    public void setCreateDate(Date createDate) {
        if (createDate != null) {
            this.createDate = (Date) createDate.clone();
        } else {
            this.createDate = null;
        }
    }

    public Date getUpdateDate() {
        if (updateDate != null) {
            return (Date) updateDate.clone();
        } else {
            return null;
        }
    }

    public void setUpdateDate(Date updateDate) {
        if (updateDate != null) {
            this.updateDate = (Date) updateDate.clone();
        } else {
            this.updateDate = null;
        }
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}
