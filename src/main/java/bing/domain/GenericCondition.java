package bing.domain;

/**
 * 通用查询条件封装Bean
 * 
 * @author IceWee
 */
public abstract class GenericCondition {

	protected Integer id;

	protected Long pageNo = 1L;

	protected Long pageSize = 10L;

	protected Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
