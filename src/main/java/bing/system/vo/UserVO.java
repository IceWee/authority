package bing.system.vo;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import bing.domain.GenericObject;

public class UserVO extends GenericObject {

	private static final long serialVersionUID = -7983749899316231902L;

	private Integer id;

	private String name;

	private String mobile;

	private Integer status;

	private String statusText;

	private String createUser;

	private Date createDate;

	private String createDateText;

	private String updateUser;

	private Date updateDate;

	private String updateDateText;

	public UserVO() {
		super();
	}

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
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusText() {
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

}
