package bing.system.condition;

import bing.domain.GenericCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysUserCondition extends GenericCondition {

	@ApiModelProperty(value = "账号", dataType = "String", example = "zhangsan")
	private String username;

	@ApiModelProperty(value = "姓名", dataType = "String", example = "张三")
	private String name;

	@ApiModelProperty(value = "手机", dataType = "String", example = "13011112222")
	private String mobile;

}
