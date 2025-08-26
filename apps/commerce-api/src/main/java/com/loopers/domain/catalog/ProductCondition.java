package com.loopers.domain.catalog;

import com.loopers.domain.user.AccountId;
import org.springframework.data.domain.Pageable;

public class ProductCondition {

    public record ListCondition(Long brandId, SortType sort, Pageable pageable) {

        public ListCondition(Long brandId, String sort, Pageable pageable) {
            this(brandId, sort != null && !sort.isBlank() ? SortType.valueOf(sort.toUpperCase()) : SortType.LATEST, pageable);
        }

        public int pageSize() {
            return pageable.getPageSize();
        }

        public long offSet() {
            return pageable.getOffset();
        }
    }

    public record DetailCondition(Long productId, AccountId accountId) {

        public DetailCondition(Long productId, String accountId) {
            this(productId, accountId != null && !accountId.isBlank() ? AccountId.of(accountId) : null);
        }
    }
}
