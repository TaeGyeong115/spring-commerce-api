package io.taylor.api.service.order;

import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.service.log.LogService;
import io.taylor.api.service.order.request.OrderServiceRequest;
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

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LogService logService;

    public void saveOrderForProduct(long customerId, OrderServiceRequest request) {
        Order order = Order.builder()
                .productId(request.productId())
                .price(request.price())
                .customerId(customerId)
                .quantity(request.quantity())
                .build();
        long orderId = orderRepository.save(order).getId();
        logService.saveLog(ActionType.CREATE, TargetType.ORDER, customerId, orderId);
    }

    public List<OrderResponse> getOrderByMemberId(long memberId) {
        return orderRepository.findAllByCustomerId(memberId);
    }

    public List<OrderResponse> findByProductIdAndProviderId(long productId, long providerId) {
        return orderRepository.findByProductIdAndProviderId(productId, providerId);
    }

    public OrderResponse getOrderByIdAndMemberId(long memberId, long orderId) {
        return orderRepository.findByIdAndCustomerId(orderId, memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."));
    }

    public void deleteOrderStatus(long memberId, long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않습니다."));
        if (order.getCustomerId() != memberId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
        }

        order.updateStatue(OrderStatus.CANCELED);
        orderRepository.save(order);
        logService.saveLog(ActionType.DELETE, TargetType.ORDER, memberId, orderId);
    }

    public void updateOrderStatus(OrderStatus status, long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않습니다."));
        order.updateStatue(status);
        orderRepository.save(order);
    }
}
