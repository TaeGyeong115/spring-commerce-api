package io.taylor.api.service.order;

import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.api.service.log.LogService;
import io.taylor.domain.log.ActionType;
import io.taylor.domain.log.TargetType;
import io.taylor.domain.order.Order;
import io.taylor.domain.order.OrderRepository;
import io.taylor.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LogService logService;

    public long saveOrderForProduct(long customerId, ProductResponse response) {
        Order order = Order.builder()
                .productId(response.id())
                .price(response.price())
                .customerId(customerId)
                .quantity(response.quantity())
                .build();
        return orderRepository.save(order).getId();
    }

    public List<OrderResponse> getOrderByMemberId(long memberId) {
        return orderRepository.findAllByCustomerId(memberId);
    }

    public OrderResponse getOrderByIdAndMemberId(long memberId, long orderId) {
        return orderRepository.findByIdAndCustomerId(orderId, memberId);
    }

    public Optional<Order> getOrderById(long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<OrderResponse> findByProductIdAndProviderId(Long productId, Long providerId) {
        return orderRepository.findByProductIdAndProviderId(productId, providerId);
    }

    public void deleteOrderStatus(AuthenticatedMember member, long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 주문입니다."));
        if (order.getCustomerId() != member.memberId() || order.getProductId() != member.memberId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
        }

        order.updateStatue(OrderStatus.COMPLETED);
        orderRepository.save(order);
        logService.saveLog(ActionType.DELETE, TargetType.ORDER, member.memberId(), orderId);
    }

    public void updateOrderStatus(OrderStatus status, long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 주문입니다."));
        order.updateStatue(status);
        orderRepository.save(order);
    }
}
