package io.taylor.domain.order;

import io.taylor.api.controller.order.response.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryQuery {

    List<OrderResponse> findAllByCustomerId(long customerId);

    List<OrderResponse> findByProductIdAndProviderId(long productId, long providerId);

    Optional<OrderResponse> findByIdAndCustomerId(Long orderId, long member);
}
