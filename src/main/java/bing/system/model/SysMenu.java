package bing.system.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import bing.domain.GenericObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysMenu extends GenericObject implements Serializable {

	private static final long serialVersionUID = -245722492846076538L;

	private Integer id;

	@NotBlank(message = "{name.required}")
	private String name;

	@NotNull(message = "{menu.parent.required}")
	private Integer parentId;

	private Integer resourceId;

	private Integer sort = 1;

	private String remark;

}