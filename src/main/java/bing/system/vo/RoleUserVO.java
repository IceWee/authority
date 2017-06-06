package bing.system.vo;

import java.util.ArrayList;
import java.util.List;

import bing.system.model.SysUser;
import io.swagger.annotations.ApiModelProperty;
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

	@ApiModelProperty(value = "角色ID", dataType = "Integer", required = true, position = 1, example = "1")
	private Integer roleId;

	@ApiModelProperty(value = "未选用户", dataType = "List", required = true, position = 2)
	private List<SysUser> unselectUsers = new ArrayList<>();

	@ApiModelProperty(value = "已选用户", dataType = "List", required = true, position = 3)
	private List<SysUser> selectedUsers = new ArrayList<>();

}
