package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.OrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Order;
import io.taylor.wantedpreonboardingchallengebackend20.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderResponse> getOrderByMemberId(long memberId) {
        List<Order> orders = orderRepository.findAllByCustomerId(memberId);
        return orders.stream()
                .map(entity -> new OrderResponse(entity.getId(), "", entity.getQuantity(), entity.getPrice(), entity.getTotalPrice(), entity.getStatus(), entity.getUpdatedAt(), entity.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.map(order ->
                        new OrderResponse(order.getId(), "", order.getQuantity(), order.getPrice(), order.getTotalPrice(), order.getStatus(), order.getUpdatedAt(), order.getCreatedAt())
                )
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }

    public void updateOrder(AuthenticatedMember member, long orderId, ProductOrderRequest request) {
    }
}
