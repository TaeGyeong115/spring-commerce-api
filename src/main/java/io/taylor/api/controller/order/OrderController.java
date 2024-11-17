package io.taylor.api.controller.order;

import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        OrderResponse response = orderService.getOrderByIdAndMemberId(authenticatedMember.memberId(), orderId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(AuthenticatedMember authenticatedMember,
                                                  @PathVariable("orderId") Long orderId) {
        orderService.deleteOrderStatus(authenticatedMember, orderId);
        return ResponseEntity.ok().build();
    }

}