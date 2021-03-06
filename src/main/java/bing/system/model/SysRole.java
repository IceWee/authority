package bing.system.model;

import bing.constant.VisibilityEnum;
import bing.domain.GenericObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class SysRole extends GenericObject implements Serializable {

	private static final long serialVersionUID = 745422160006062721L;

	private Integer id;

	@NotBlank(message = "{code.required}")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{code.illegal}")
	private String code;

	@NotBlank(message = "{name.required}")
	private String name;

	private String remark;

	private Integer hidden;

	public Integer getHidden() {
		if (hidden == null) {
			hidden = VisibilityEnum.HIDDEN.ordinal();
		}
		return hidden;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		SysRole other = (SysRole) obj;
		if (id == null) {
			if (other.id != null) {
                return false;
            }
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}