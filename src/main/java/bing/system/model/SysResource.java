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
public class SysResource extends GenericObject implements Serializable {

	private static final long serialVersionUID = -6827672328719969042L;

	private Integer id;

	@NotNull(message = "{name.required}")
	private String name;

	@NotNull(message = "{category.required}")
	private Integer categoryId;

	private Integer type;

	@NotNull(message = "{url.required}")
	private String url;

	private String remark;

}