package bing.system.vo;

import bing.domain.GenericVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysRoleVO extends GenericVO {

	private static final long serialVersionUID = 7276604397939483725L;

	private String code;

	private String name;

	private String remark;

}
