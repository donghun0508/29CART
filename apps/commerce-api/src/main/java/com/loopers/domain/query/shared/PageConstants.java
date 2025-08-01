package com.loopers.domain.query.shared;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageConstants {

    public static int DEFAULT_PAGE_NUMBER = 0;
    public static int DEFAULT_PAGE_SIZE = 20;

    public static Pageable defaultPageable() {
        return PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
    }
}
