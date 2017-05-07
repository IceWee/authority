package bing.system.vo;

import bing.domain.GenericVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysUserVO extends GenericVO {

	private static final long serialVersionUID = -7983749899316231902L;

	private String username;

	private String name;

	private String mobile;

}
