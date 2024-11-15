package io.taylor.api.controller.product;

import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.order.request.OrderRequest;
import io.taylor.api.controller.order.request.OrderStatusRequest;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.controller.product.request.ProductRequest;
import io.taylor.api.controller.product.response.OwnedProductResponse;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.api.service.product.ProductService;
import io.taylor.domain.order.OrderStatus;
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
    public ResponseEntity<Object> createProduct(AuthenticatedMember authenticatedMember,
                                                @Valid @RequestBody ProductRequest request) {
        productService.createProduct(authenticatedMember, request.toServiceRequest());
        return ResponseEntity.status(HttpStatus.CREATED).build();
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

    @GetMapping("/owned")
    public ResponseEntity<List<OwnedProductResponse>> findOwnedProducts(AuthenticatedMember authenticatedMember) {
        List<OwnedProductResponse> response = productService.findOwnedProducts(authenticatedMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owned/{productId}")
    public ResponseEntity<List<OrderResponse>> findByProductIdAndProviderId(AuthenticatedMember authenticatedMember, @PathVariable("productId") Long productId) {
        List<OrderResponse> response = productService.findByProductIdAndProviderId(authenticatedMember, productId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/owned/{productId}")
    public ResponseEntity<Void> updateProductStatus(AuthenticatedMember authenticatedMember, @PathVariable("productId") Long productId) {
        productService.updateProductStatus(authenticatedMember, productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/owned/order/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(AuthenticatedMember authenticatedMember, @PathVariable("orderId") Long orderId, @Valid @RequestBody OrderStatusRequest request) {
        productService.updateOrderStatus(authenticatedMember, orderId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}