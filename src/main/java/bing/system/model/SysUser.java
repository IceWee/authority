package bing.system.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import bing.constant.HiddenEnum;
import bing.constant.StatusEnum;
import bing.security.CustomUserDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysUser extends CustomUserDetails {

	private static final long serialVersionUID = 5217735149888451989L;

	private Integer id;

	@NotNull(message = "{surname.required}")
	private String name;

	@Pattern(regexp = "^$|^(13|14|15|17|18)[0-9]{9}$", message = "{mobile.illegal}")
	private String mobile;

	private Integer hidden;

	private Integer[] roleIds;

	public SysUser(String username, String password) {
		super(username, password);
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

	public Integer getHidden() {
		if (hidden == null) {
			hidden = HiddenEnum.HIDDEN.ordinal();
		}
		return hidden;
	}

}
