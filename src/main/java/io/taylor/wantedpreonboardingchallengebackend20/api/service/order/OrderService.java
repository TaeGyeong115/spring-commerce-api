package io.taylor.wantedpreonboardingchallengebackend20.api.service.order;

import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.order.response.OrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderResponse> getOrderByMemberId(long memberId) {
        return orderRepository.findAllByCustomerId(memberId);
    }

    public OrderResponse getOrderById(long memberId, long orderId) {
        return orderRepository.findById(orderId, memberId);
    }

    public void updateOrder(AuthenticatedMember member, long orderId, ProductOrderRequest request) {
    }
}
