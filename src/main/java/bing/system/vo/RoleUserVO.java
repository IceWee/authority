package bing.system.vo;

import java.util.ArrayList;
import java.util.List;

import bing.system.model.SysUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 角色用户值对象
 * 
 * @author IceWee
 */
@Getter
@Setter
@NoArgsConstructor
public class RoleUserVO {

	private Integer roleId;

	private List<SysUser> unselectUsers = new ArrayList<>();

	private List<SysUser> selectedUsers = new ArrayList<>();

}
