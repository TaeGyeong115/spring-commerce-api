package io.taylor.domain.order;

import io.taylor.api.controller.order.response.OrderResponse;

import java.util.List;

public interface OrderRepositoryQuery {

    List<OrderResponse> findAllByCustomerId(long customerId);

    List<OrderResponse> findByProductIdAndProviderId(long productId, long providerId);

    OrderResponse findByIdAndCustomerId(Long orderId, long member);
}
