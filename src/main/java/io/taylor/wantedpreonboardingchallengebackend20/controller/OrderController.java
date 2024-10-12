package io.taylor.wantedpreonboardingchallengebackend20.controller;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.ProductOrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.ProductResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Order;
import io.taylor.wantedpreonboardingchallengebackend20.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(AuthenticatedMember authenticatedMember) {
        List<Order> orders = orderService.getOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ProductOrderResponse> getOrder(AuthenticatedMember authenticatedMember, @PathVariable("orderId") Long orderId) {
        ProductOrderResponse response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<ProductResponse> updateOrder(AuthenticatedMember authenticatedMember, @PathVariable("orderId") Long orderId,
                                                       @RequestBody ProductOrderRequest request) {
        ProductResponse response = orderService.updateOrder(orderId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}