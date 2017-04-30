package bing.domain;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import bing.constant.StatusEnum;

public class GenericVO extends GenericObject {

	protected static final long serialVersionUID = -6547881868397315449L;

	protected Integer id;

	protected Integer status;

	protected String statusText;

	protected String createUser;

	protected Date createDate;

	protected String createDateText;

	protected String updateUser;

	protected Date updateDate;

	protected String updateDateText;

	public GenericVO() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateDateText() {
		if (createDate != null) {
			createDateText = DateFormatUtils.format(createDate, "yyyy-MM-dd");
		}
		return createDateText;
	}

	public void setCreateDateText(String createDateText) {
		this.createDateText = createDateText;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateDateText() {
		if (updateDate != null) {
			updateDateText = DateFormatUtils.format(updateDate, "yyyy-MM-dd");
		}
		return updateDateText;
	}

	public void setUpdateDateText(String updateDateText) {
		this.updateDateText = updateDateText;
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
