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
    public ResponseEntity<List<OrderResponse>> getAllOrders(AuthenticatedMember member) {
        List<OrderResponse> orders = orderService.getOrderByMemberId(member.memberId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(AuthenticatedMember member, @PathVariable("orderId") Long orderId) {
        OrderResponse response = orderService.getOrderByIdAndMemberId(member.memberId(), orderId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(AuthenticatedMember member,
                                                  @PathVariable("orderId") Long orderId) {
        orderService.deleteOrderStatus(member.memberId(), orderId);
        return ResponseEntity.ok().build();
    }

}