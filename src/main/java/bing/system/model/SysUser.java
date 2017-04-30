package bing.system.model;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import bing.constant.HiddenEnum;
import bing.constant.StatusEnum;
import bing.security.CustomUserDetails;

public class SysUser extends CustomUserDetails {

	private static final long serialVersionUID = 5217735149888451989L;

	private Integer id;

	@NotBlank(message = "{surname.required}")
	private String name;

	@Pattern(regexp = "^$|^(13|14|15|17|18)[0-9]{9}$", message = "{mobile.illegal}")
	private String mobile;

	private Integer hidden = HiddenEnum.HIDDEN.ordinal();

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

	public Integer getHidden() {
		return hidden;
	}

	public void setHidden(Integer hidden) {
		this.hidden = hidden;
	}

	@Override
	public boolean isAccountNonExpired() {
		return StatusEnum.NORMAL.ordinal() == status;
	}

	@Override
	public boolean isAccountNonLocked() {
		return StatusEnum.LOCKED.ordinal() != status;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return StatusEnum.NORMAL.ordinal() == status;
	}

	@Override
	public boolean isEnabled() {
		return StatusEnum.NORMAL.ordinal() == status;
	}

}
