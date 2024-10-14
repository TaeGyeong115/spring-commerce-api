package io.taylor.wantedpreonboardingchallengebackend20.repository;

import io.taylor.wantedpreonboardingchallengebackend20.dto.response.OrderResponse;

import java.util.List;

public interface OrderRepositoryQuery {

    List<OrderResponse> findAllByCustomerId(long customerId);

    OrderResponse findById(long member, long orderId);
}
