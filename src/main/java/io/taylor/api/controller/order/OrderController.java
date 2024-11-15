package io.taylor.api.controller.order;

import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.order.request.OrderRequest;
import io.taylor.api.controller.order.request.OrderStatusRequest;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<Object> createOrderForProduct(AuthenticatedMember authenticatedMember,
                                                        @Valid @RequestBody OrderRequest request) {
        orderService.createOrderForProduct(authenticatedMember, request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

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
    public ResponseEntity<Void> updateOrderStatus(AuthenticatedMember authenticatedMember,
                                            @PathVariable("orderId") Long orderId,
                                            @Valid @RequestBody OrderStatusRequest request) {
        orderService.updateOrderStatus(authenticatedMember, orderId, request);
        return ResponseEntity.ok().build();
    }

}