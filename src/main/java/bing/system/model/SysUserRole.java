package bing.system.model;

import java.io.Serializable;

import bing.domain.GenericObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysUserRole extends GenericObject implements Serializable {

	private static final long serialVersionUID = -2183035361371500068L;

	private Integer id;

	private Integer userId;

	private Integer roleId;

	public SysUserRole(Integer userId, Integer roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

}