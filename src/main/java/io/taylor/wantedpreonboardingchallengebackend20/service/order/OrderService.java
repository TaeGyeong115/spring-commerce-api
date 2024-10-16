package io.taylor.wantedpreonboardingchallengebackend20.service.order;

import io.taylor.wantedpreonboardingchallengebackend20.dto.member.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderRepository;
import io.taylor.wantedpreonboardingchallengebackend20.dto.product.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.order.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderResponse> getOrderByMemberId(long memberId) {
        return orderRepository.findAllByCustomerId(memberId);
    }

    public OrderResponse getOrderById(long memberId, long orderId) {
        return orderRepository.findById(orderId, memberId);
    }

    public void updateOrder(AuthenticatedMember member, long orderId, ProductOrderRequest request) {
    }
}
