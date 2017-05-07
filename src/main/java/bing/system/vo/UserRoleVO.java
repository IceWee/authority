package bing.system.vo;

import java.util.ArrayList;
import java.util.List;

import bing.system.model.SysRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户角色值对象
 * 
 * @author IceWee
 */
@Getter
@Setter
@NoArgsConstructor
public class UserRoleVO {

	private Integer userId;

	private List<SysRole> unselectRoles = new ArrayList<>();

	private List<SysRole> selectedRoles = new ArrayList<>();

}
