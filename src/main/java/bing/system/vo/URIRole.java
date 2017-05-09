package bing.system.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class URIRole {

	private Integer id;

	private String uri;

	/**
	 * 角色编码
	 */
	private String roleCode;

}
