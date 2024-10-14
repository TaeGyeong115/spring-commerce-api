package io.taylor.wantedpreonboardingchallengebackend20.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static io.taylor.wantedpreonboardingchallengebackend20.entity.QOrder.order;
import static io.taylor.wantedpreonboardingchallengebackend20.entity.QProduct.product;

@RequiredArgsConstructor
public class OrderRepositoryQueryImpl implements OrderRepositoryQuery {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderResponse> findAllByCustomerId(long customerId) {
        return jpaQueryFactory.select(Projections.constructor(OrderResponse.class, order.id, product.name, order.quantity, order.price, order.totalPrice, order.status, order.updatedAt, order.createdAt))
                .from(order)
                .join(product).on(order.productId.eq(product.id))
                .where(order.customerId.eq(customerId))
                .fetch();
    }

    @Override
    public OrderResponse findById(long memberId, long orderId) {
        return jpaQueryFactory.select(Projections.constructor(OrderResponse.class, order.id, product.name, order.quantity, order.price, order.totalPrice, order.status, order.updatedAt, order.createdAt))
                .from(order)
                .join(product).on(order.productId.eq(product.id))
                .where(order.id.eq(orderId).and(order.customerId.eq(memberId)))
                .fetchOne();
    }

}
