package bing.system.model;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import bing.constant.HiddenEnum;
import bing.constant.StatusEnum;
import bing.domain.CrudGroups;
import bing.security.CustomUserDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysUser extends CustomUserDetails {

	private static final long serialVersionUID = 5217735149888451989L;

	@ApiModelProperty(value = "主键", dataType = "Integer", example = "1")
	private Integer id;

	@ApiModelProperty(value = "姓名", dataType = "String", required = true, example = "张三")
	@NotBlank(groups = {CrudGroups.Create.class, CrudGroups.Update.class}, message = "{surname.required}")
	private String name;

	@ApiModelProperty(value = "手机", dataType = "String", example = "13011112222")
	@Pattern(groups = {CrudGroups.Create.class, CrudGroups.Update.class}, regexp = "^$|^(13|14|15|17|18)[0-9]{9}$", message = "{mobile.illegal}")
	private String mobile;

	@ApiModelProperty(hidden = true)
	private Integer hidden;

	@ApiModelProperty(hidden = true)
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
