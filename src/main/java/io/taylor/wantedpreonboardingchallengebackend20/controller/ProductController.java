package io.taylor.wantedpreonboardingchallengebackend20.controller;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductRequest;
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
    public ResponseEntity<ProductResponse> createProduct(AuthenticatedMember authenticatedMember, @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(authenticatedMember, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        ProductResponse response = productService.getProductById(productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/orders")
    public ResponseEntity<ProductResponse> createOrder(AuthenticatedMember authenticatedMember, @PathVariable Long productId, @RequestBody ProductRequest request) {
        ProductResponse response = productService.createOrderForProduct(authenticatedMember, productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{productId}/orders")
    public ResponseEntity<ProductResponse> getOrder(AuthenticatedMember authenticatedMember, @PathVariable Long productId) {
        ProductResponse response = productService.getOrderForProduct(authenticatedMember, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}