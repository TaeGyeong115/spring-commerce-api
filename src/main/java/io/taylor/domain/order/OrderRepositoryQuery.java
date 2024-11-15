package io.taylor.domain.order;

import io.taylor.api.controller.order.response.OrderResponse;

import java.util.List;

public interface OrderRepositoryQuery {

    List<OrderResponse> findAllByCustomerId(long customerId);

    List<OrderResponse> findByProductId(long productId);

    OrderResponse findById(Long orderId, long member);
}
