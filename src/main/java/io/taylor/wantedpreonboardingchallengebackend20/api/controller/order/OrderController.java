package io.taylor.wantedpreonboardingchallengebackend20.api.controller.order;

import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.order.response.OrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(AuthenticatedMember authenticatedMember) {
        List<OrderResponse> orders = orderService.getOrderByMemberId(authenticatedMember.memberId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(AuthenticatedMember authenticatedMember, @PathVariable("orderId") Long orderId) {
        OrderResponse response = orderService.getOrderById(authenticatedMember.memberId(), orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Object> updateOrder(AuthenticatedMember authenticatedMember, @PathVariable("orderId") Long orderId, @RequestBody ProductOrderRequest request) {
        orderService.updateOrder(authenticatedMember, orderId, request);
        return ResponseEntity.noContent().build();
    }

}