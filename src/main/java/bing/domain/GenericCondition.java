package bing.domain;

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

	protected Integer id;

	protected int pageNumber = 1;

	protected int pageSize = 10;

	protected Integer status;

}
