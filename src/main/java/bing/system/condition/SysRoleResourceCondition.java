package bing.system.condition;

import bing.domain.GenericCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysRoleResourceCondition extends GenericCondition {

	private Integer roleId;

	private Integer resourceId;
}
