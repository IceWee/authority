package bing.domain;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author IceWee
 */
public class GenericPage<T> {

	public final static int DEFAULT_PAGE_SIZE = 10;

	@ApiModelProperty(value = "页号", required = true, dataType = "Integer", example = "1")
	protected int pageNumber = 1;

	@ApiModelProperty(value = "每页显示记录数", required = true, dataType = "Integer", example = "10")
	protected int pageSize = DEFAULT_PAGE_SIZE;

	@ApiModelProperty(value = "总记录数", required = true, dataType = "Integer", example = "100")
	protected long total = 0;

	@ApiModelProperty(value = "数据列表")
	protected List<T> rows = new ArrayList<>();

	public GenericPage() {
		super();
	}

	public GenericPage(long total, List<T> rows) {
		this();
		this.total = total;
		this.rows = rows;
	}

	public GenericPage(int pageNumber, int pageSize, long total, List<T> rows) {
		this(total, rows);
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}

	public long getTotalPages() {
		long totalPages = total / pageSize;
		if (total % pageSize > 0) {
			totalPages++;
		}
		return totalPages;
	}

	public long getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
		if (pageNumber < 1) {
			this.pageNumber = 1;
		}
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}