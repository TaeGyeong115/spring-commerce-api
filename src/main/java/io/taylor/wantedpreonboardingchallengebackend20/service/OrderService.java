package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.OrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Order;
import io.taylor.wantedpreonboardingchallengebackend20.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    public List<OrderResponse> getOrderByMemberId(long memberId) {
        List<Order> orders = orderRepository.findAllByCustomerId(memberId);
        List<OrderResponse> response = orders.stream()
                .map(entity -> new OrderResponse(entity.toString()))
                .collect(Collectors.toList());

        return response;
    }

    public OrderResponse getOrderById(long orderId) {
        return orderRepository.findById(orderId)
                .map(existOrder -> new OrderResponse("test"))
                .orElseGet(() -> new OrderResponse(null));
    }

    public OrderResponse updateOrder(long orderId, ProductOrderRequest request) {
        return null;
    }
}
