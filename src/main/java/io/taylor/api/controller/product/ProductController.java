package io.taylor.api.controller.product;

import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.order.request.OrderStatusRequest;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.controller.product.request.OrderProductRequest;
import io.taylor.api.controller.product.request.ProductRequest;
import io.taylor.api.controller.product.response.OwnedProductResponse;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.api.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> saveProduct(AuthenticatedMember member,
                                            @Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.saveProduct(member.memberId(), request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProducts() {
        List<ProductResponse> responses = productService.findAllProducts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable("productId") Long productId) {
        ProductResponse response = productService.findProductById(productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<OrderResponse> orderProduct(AuthenticatedMember member,
                                             @PathVariable("productId") Long productId,
                                             @Valid @RequestBody OrderProductRequest request) {
        OrderResponse response = productService.orderProduct(member.memberId(), productId, request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/owned")
    public ResponseEntity<List<OwnedProductResponse>> findOwnedProducts(AuthenticatedMember member) {
        List<OwnedProductResponse> response = productService.findOwnedProducts(member.memberId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owned/{productId}")
    public ResponseEntity<List<OrderResponse>> findByProductIdAndProviderId(AuthenticatedMember member, @PathVariable("productId") Long productId) {
        List<OrderResponse> response = productService.findOrderByProductIdAndProviderId(member.memberId(), productId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/owned/{productId}")
    public ResponseEntity<Void> updateProductStatus(AuthenticatedMember member, @PathVariable("productId") Long productId) {
        productService.updateProductStatus(member.memberId(), productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/owned/{productId}/order")
    public ResponseEntity<Void> updateOrderStatus(AuthenticatedMember member, @PathVariable("productId") Long productId, @Valid @RequestBody OrderStatusRequest request) {
        productService.updateOrderStatus(member.memberId(), productId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}