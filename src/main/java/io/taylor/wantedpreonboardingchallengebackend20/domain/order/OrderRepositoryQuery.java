package io.taylor.wantedpreonboardingchallengebackend20.domain.order;

import io.taylor.wantedpreonboardingchallengebackend20.controller.order.response.OrderResponse;

import java.util.List;

public interface OrderRepositoryQuery {

    List<OrderResponse> findAllByCustomerId(long customerId);

    OrderResponse findById(Long orderId, long member);
}
