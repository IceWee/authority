package bing.system.vo;

import bing.domain.GenericVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysMenuVO extends GenericVO {

	private static final long serialVersionUID = -5434727960404808904L;

	private String name;

	private Integer parentId;

	private Integer resourceId;

	private String iconClass;

	private Integer sort;

	private String remark;

	private String url;

}
