package bing.system.condition;

import bing.domain.GenericCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysMenuCondition extends GenericCondition {

	private String name;

	private Integer parentId;

}
