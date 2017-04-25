package bing.domain;

/**
 * 通用查询条件封装Bean
 * 
 * @author IceWee
 */
public abstract class GenericCondition {

	protected Long id;
	protected Long pageNo = 1L;
	protected Long pageSize = 10L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPageNo() {
		return pageNo;
	}

	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

}
