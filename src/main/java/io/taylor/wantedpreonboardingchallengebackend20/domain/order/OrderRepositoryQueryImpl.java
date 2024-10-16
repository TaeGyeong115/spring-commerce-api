package io.taylor.wantedpreonboardingchallengebackend20.domain.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static io.taylor.wantedpreonboardingchallengebackend20.domain.order.QOrder.order;
import static io.taylor.wantedpreonboardingchallengebackend20.domain.product.QProduct.product;

@RequiredArgsConstructor
public class OrderRepositoryQueryImpl implements OrderRepositoryQuery {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderResponse> findAllByCustomerId(long customerId) {
        return baseOrderQuery()
                .where(order.customerId.eq(customerId))
                .fetch();
    }

    @Override
    public OrderResponse findById(Long orderId, long memberId) {
        return baseOrderQuery()
                .where(order.id.eq(orderId).and(order.customerId.eq(memberId)))
                .fetchOne();
    }

    private JPAQuery<OrderResponse> baseOrderQuery() {
        return jpaQueryFactory
                .select(Projections.constructor(
                        OrderResponse.class,
                        order.id,
                        product.name,
                        order.quantity,
                        order.price,
                        order.totalPrice,
                        order.status,
                        order.modifiedDate,
                        order.createdDate
                ))
                .from(order)
                .join(product).on(order.productId.eq(product.id));
    }
}
