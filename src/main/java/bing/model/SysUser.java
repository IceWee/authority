package bing.model;

import java.util.Date;

import bing.constants.Hidden;
import bing.constants.UserStatus;
import bing.domain.BaseUser;

public class SysUser extends BaseUser {

	private static final long serialVersionUID = 5217735149888451989L;

	private Integer id;

	private String name;

	private String mobile;

	private Integer status = UserStatus.NORMAL.ordinal();

	private Integer hidden = Hidden.HIDDEN.ordinal();

	private String createUser;

	private Date createDate;

	private String updateUser;

	private Date updateDate;

	public SysUser() {
		super();
	}

	public SysUser(String username, String password) {
		super(username, password);
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

	public Integer getHidden() {
		return hidden;
	}

	public void setHidden(Integer hidden) {
		this.hidden = hidden;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
	public boolean isAccountNonExpired() {
		return UserStatus.NORMAL.ordinal() == status;
	}

	@Override
	public boolean isAccountNonLocked() {
		return UserStatus.LOCKED.ordinal() != status;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return UserStatus.NORMAL.ordinal() == status;
	}

	@Override
	public boolean isEnabled() {
		return UserStatus.NORMAL.ordinal() == status;
	}

}
