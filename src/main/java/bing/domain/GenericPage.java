package bing.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author IceWee
 */
public class GenericPage<T> {

	public final static int DEFAULT_PAGE_SIZE = 10;

	protected int pageNumber = 1;
	protected int pageSize = DEFAULT_PAGE_SIZE;
	protected long total = 0;
	protected List<T> rows = new ArrayList<>();

	public GenericPage() {
		super();
	}

	public GenericPage(long total, List<T> rows) {
		this();
		this.total = total;
		this.rows = rows;
	}

	public GenericPage(int pageSize, long total, List<T> rows) {
		this(total, rows);
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

	public long getPreviousPageNumber() {
		if (hasPreviousPage()) {
			return pageNumber - 1;
		} else {
			return pageNumber;
		}
	}

	public long getNextPageNumber() {
		if (hasNextPage()) {
			return pageNumber + 1;
		} else {
			return pageNumber;
		}
	}

	public boolean hasPreviousPage() {
		return (pageNumber - 1 >= 1);
	}

	public boolean hasNextPage() {
		return (pageNumber + 1 <= getTotalPages());
	}

}