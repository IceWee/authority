package bing.system.condition;

import bing.domain.GenericCondition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysResourceCondition extends GenericCondition {

	private String name;

	private Integer type;

	private Integer categoryId;

}
