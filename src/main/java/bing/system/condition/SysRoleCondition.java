package bing.system.condition;

import bing.domain.GenericCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysRoleCondition extends GenericCondition {

	private String code;

	private String name;

}
