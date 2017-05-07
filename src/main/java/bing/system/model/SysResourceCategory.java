package bing.system.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import bing.domain.GenericObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysResourceCategory extends GenericObject implements Serializable {

	private static final long serialVersionUID = -335302522344473301L;

	private Integer id;

	@NotNull(message = "{name.required}")
	private String name;

	private Integer parentId;

	private String remark;

}