package io.taylor.domain.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.domain.product.QProduct;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderRepositoryQueryImpl implements OrderRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderResponse> findAllByCustomerId(long customerId) {
        return baseOrderQuery()
                .where(QOrder.order.customerId.eq(customerId))
                .fetch();
    }

    @Override
    public List<OrderResponse> findByProductIdAndProviderId(long productId, long providerId) {
        return baseOrderQuery()
                .where(QOrder.order.productId.eq(productId).and(QProduct.product.providerId.eq(providerId)))
                .fetch();
    }

    @Override
    public Optional<OrderResponse> findByIdAndCustomerId(Long orderId, long memberId) {
        return Optional.ofNullable(baseOrderQuery()
                .where(QOrder.order.id.eq(orderId).and(QOrder.order.customerId.eq(memberId)))
                .fetchOne());
    }

    private JPAQuery<OrderResponse> baseOrderQuery() {
        return jpaQueryFactory
                .select(Projections.constructor(
                        OrderResponse.class,
                        QOrder.order.id,
                        QProduct.product.name,
                        QOrder.order.quantity,
                        QOrder.order.price,
                        QOrder.order.totalPrice,
                        QOrder.order.status,
                        QOrder.order.modifiedDateTime,
                        QOrder.order.createdDateTime
                ))
                .from(QOrder.order)
                .join(QProduct.product).on(QOrder.order.productId.eq(QProduct.product.id));
    }
}
