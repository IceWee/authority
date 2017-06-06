package bing.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 通用查询条件封装Bean
 * 
 * @author IceWee
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class GenericCondition {

	@ApiModelProperty(value = "主键", hidden = true)
	protected Integer id;

	@ApiModelProperty(value = "页号", example = "1")
	protected int pageNumber = 1;

	@ApiModelProperty(value = "每页显示记录数", example = "10")
	protected int pageSize = 10;

	@ApiModelProperty(value = "状态")
	protected Integer status;

}
