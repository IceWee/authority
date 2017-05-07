package bing.system.condition;

import bing.domain.GenericCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysUserCondition extends GenericCondition {

	private String username;

	private String name;

	private String mobile;

}
