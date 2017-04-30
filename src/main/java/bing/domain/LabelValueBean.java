package bing.domain;

/**
 * 仿照Struts的LabelValueBean，主要用于封装下拉列表
 * 
 * @author IceWee
 */
public class LabelValueBean extends GenericObject {

	private static final long serialVersionUID = -14923110827133465L;

	private String label;
	private String value;

	public LabelValueBean() {
		super();
	}

	public LabelValueBean(String label, String value) {
		super();
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
