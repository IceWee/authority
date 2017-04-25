package bing.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author IceWee
 */
public class GenericPage<T> {

	public final static long DEFAULT_PAGE_SIZE = 10;

	protected long pageNo = 1;
	protected long pageSize = DEFAULT_PAGE_SIZE;
	protected long totalRows = 0;
	protected List<T> data = new ArrayList<>();

	public GenericPage() {
		super();
	}

	public GenericPage(long totalRows, List<T> data) {
		this();
		this.totalRows = totalRows;
		this.data = data;
	}

	public GenericPage(long pageSize, long totalRows, List<T> data) {
		this(totalRows, data);
		this.pageSize = pageSize;
	}

	public long getTotalPages() {
		long totalPages = totalRows / pageSize;
		if (totalRows % pageSize > 0) {
			totalPages++;
		}
		return totalPages;
	}

	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getPreviousPageNo() {
		if (hasPreviousPage()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}

	public long getNextPageNo() {
		if (hasNextPage()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	public boolean hasPreviousPage() {
		return (pageNo - 1 >= 1);
	}

	public boolean hasNextPage() {
		return (pageNo + 1 <= getTotalPages());
	}

}