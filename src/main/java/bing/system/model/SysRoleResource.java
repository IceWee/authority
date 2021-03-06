package bing.system.model;

import java.io.Serializable;

import bing.domain.GenericObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysRoleResource extends GenericObject implements Serializable {

	private static final long serialVersionUID = -395684170681026152L;

	private Integer id;

	private Integer roleId;

	private Integer resourceId;

	public SysRoleResource(Integer roleId, Integer resourceId) {
		super();
		this.roleId = roleId;
		this.resourceId = resourceId;
	}

}