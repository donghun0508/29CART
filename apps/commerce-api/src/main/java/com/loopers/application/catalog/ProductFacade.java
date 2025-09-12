package com.loopers.application.catalog;

import com.loopers.application.catalog.ProductResult.ProductDetailResult;
import com.loopers.application.catalog.ProductResult.ProductSliceRankingResult;
import com.loopers.application.catalog.ProductResult.ProductSliceResult;
import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductCondition.DetailCondition;
import com.loopers.domain.catalog.ProductCondition.ListCondition;
import com.loopers.domain.catalog.ProductEvent.ProductDetailViewedEvent;
import com.loopers.domain.catalog.ProductRead;
import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.catalog.ProductSliceRead;
import com.loopers.domain.catalog.Products;
import com.loopers.domain.heart.HeartService;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderNumber;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.user.UserService;
import com.loopers.event.outbox.EventStore;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductFacade {

    private final OrderService orderService;
    private final ProductService productService;
    private final HotProductKeyGenerator hotProductKeyGenerator;
    private final UserService userService;
    private final HeartService heartService;
    private final EventStore eventStore;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Slice<ProductSliceResult> getProductList(ListCondition condition) {
        ProductSliceRead productSliceRead = productService.getProductSliceRead(condition);
        return ProductSliceResult.from(productSliceRead);
    }

    public Slice<ProductSliceRankingResult> getHotProducts(String date, Pageable pageable) {
        String key = hotProductKeyGenerator.generate(date);

        int offset = (int) pageable.getOffset();
        int size   = pageable.getPageSize();
        int end    = offset + size - 1;

        List<Long> hotProductIds = productService.getHotProducts(key, offset, end);
        if (hotProductIds.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageable, false);
        }

        List<Product> products = productService.findAllOrderBy(hotProductIds);
        return ProductSliceRankingResult.from(Objects.requireNonNull(ProductSliceRead.from(products)));
    }

    public ProductDetailResult getProductDetail(DetailCondition condition) {
        ProductRead productRead = productService.getProductRead(condition.productId());

        productRead.setLiked(userService.lookupByAccountId(condition.accountId())
            .map(user -> heartService.isLikedBy(user.getId(), productRead.toTarget()))
            .orElse(false));

        Integer rank = productService.getRanking(hotProductKeyGenerator.generate(), productRead.getId());
        applicationEventPublisher.publishEvent(new ProductDetailViewedEvent(productRead));
        return ProductDetailResult.from(productRead, rank);
    }

    @Transactional
    public void restore(OrderNumber orderNumber) {
        Order order = orderService.findByOrderNumber(orderNumber);
        Products products = productService.findAllCollection(order.purchaseProductIds());

        products.restore(order.purchaseProducts());
        products.getProducts().forEach(eventStore::save);
    }

    @Transactional
    public void incrementHeart(Long productId) {
        Product product = productService.findByIdWithLock(productId);
        product.heart();
        eventStore.save(product);
    }

    @Transactional
    public void decrementHeart(Long productId) {
        Product product = productService.findByIdWithLock(productId);
        product.unHeart();
        eventStore.save(product);
    }
}
