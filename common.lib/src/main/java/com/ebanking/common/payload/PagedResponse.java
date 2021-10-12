package com.ebanking.common.payload;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedResponse implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1133674771051922822L;

	@JsonProperty("page")
	private int page;

	@JsonProperty("total_size")
	private int totalSize;

	@JsonProperty("size")
	private int size;

	@JsonProperty("total_pages")
	private int totalPages;

	@JsonProperty("total_number_of_elements")
	private int totalNoElements;

	@JsonProperty("has_more")
	private boolean hasMore;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalNoElements() {
		return totalNoElements;
	}

	public void setTotalNoElements(int totalNoElements) {
		this.totalNoElements = totalNoElements;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

	public void setPagedResponseAsEmpty() {
		setPage(0);
		setTotalSize(0);
		setSize(0);
		setTotalPages(0);
		setTotalNoElements(0);
		setHasMore(false);
	}

	public PagedResponse() {

	}
}
