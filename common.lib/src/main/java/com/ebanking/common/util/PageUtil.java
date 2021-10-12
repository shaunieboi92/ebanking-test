package com.ebanking.common.util;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ebanking.common.payload.PagedResponse;

@Component
public class PageUtil {

	public static void mapPageData(PagedResponse txnPagedResponse, Page<?> page) {
		txnPagedResponse.setPage(page.getNumber());
		txnPagedResponse.setTotalPages(page.getTotalPages());
		txnPagedResponse.setSize(page.getSize());
		txnPagedResponse.setHasMore(page.hasNext());
		txnPagedResponse.setTotalNoElements(page.getNumberOfElements());
	}

}
