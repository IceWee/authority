package bing.system.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import bing.domain.GenericVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysUserVO extends GenericVO {

	private static final long serialVersionUID = -7983749899316231902L;

	@ApiModelProperty(value = "用户名", required = true, dataType = "String", example = "zhangsan")
	@Excel(name = "用户名", orderNum = "1")
	private String username;

	@ApiModelProperty(value = "姓名", required = true, dataType = "String", example = "张三")
	@Excel(name = "姓名", orderNum = "2")
	private String name;

	@ApiModelProperty(value = "手机", dataType = "String", example = "13011112222")
	@Excel(name = "手机", orderNum = "3")
	private String mobile;

}
