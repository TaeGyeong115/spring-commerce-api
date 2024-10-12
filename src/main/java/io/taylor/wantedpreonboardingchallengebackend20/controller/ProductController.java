package io.taylor.wantedpreonboardingchallengebackend20.controller;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.ProductOrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.ProductResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Product;
import io.taylor.wantedpreonboardingchallengebackend20.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(AuthenticatedMember authenticatedMember,
                                                         @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(authenticatedMember, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProducts() {
        List<ProductResponse> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable("productId") Long productId) {
        ProductResponse response = productService.findProductById(productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/orders")
    public ResponseEntity<ProductOrderResponse> createOrderForProduct(AuthenticatedMember authenticatedMember,
                                                                      @PathVariable("productId") Long productId,
                                                                      @RequestBody ProductOrderRequest request) {
        ProductOrderResponse response = productService.createOrderForProduct(authenticatedMember, productId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}/orders")
    public ResponseEntity<ProductResponse> findOrderForProduct(AuthenticatedMember authenticatedMember,
                                                               @PathVariable("productId") Long productId) {
        ProductResponse response = productService.findOrderForProduct(productId);
        return ResponseEntity.ok(response);
    }

}