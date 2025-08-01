package com.loopers.interfaces.api.shared;

import com.loopers.domain.query.shared.PageConstants;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PaginationRequest {

    private Integer page;
    private Integer size;

    public Pageable toPageable() {
        if(page == null && size == null) {
            return PageConstants.defaultPageable();
        }
        int actualPage = (page == null || page <= 0) ? 1 : page;
        int actualSize = (size == null || size <= 0) ? PageConstants.DEFAULT_PAGE_SIZE : size;
        actualSize = Math.min(actualSize, 20);

        return PageRequest.of(actualPage - 1, actualSize);
    }
}
