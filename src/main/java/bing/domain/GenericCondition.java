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

	protected Long pageNo = 1L;

	protected Long pageSize = 10L;

	protected Integer status;

}
