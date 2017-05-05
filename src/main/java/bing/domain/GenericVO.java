package bing.domain;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import bing.constant.GlobalConstants;
import bing.constant.StatusEnum;
import bing.util.StringUtils;

public class GenericVO extends GenericObject {

	protected static final long serialVersionUID = -6547881868397315449L;

	protected Integer id;

	protected Integer status;

	protected String statusText;

	protected String createUser;

	protected Date createDate;

	protected String createDateTime;

	protected String updateUser;

	protected Date updateDate;

	protected String updateDateTime;

	public GenericVO() {
		super();
	}

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
		return createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
		return updateDate;
	}

	@Override
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
