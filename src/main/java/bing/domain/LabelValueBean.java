package bing.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 仿照Struts的LabelValueBean，主要用于封装下拉列表
 * 
 * @author IceWee
 */
@Getter
@Setter
@NoArgsConstructor
public class LabelValueBean extends GenericObject {

	private static final long serialVersionUID = -14923110827133465L;

	private String label;
	private String value;

	public LabelValueBean(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}

}
