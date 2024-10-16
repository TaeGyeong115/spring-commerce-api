package io.taylor.wantedpreonboardingchallengebackend20.controller.product;

import io.taylor.wantedpreonboardingchallengebackend20.controller.member.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.controller.product.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.controller.product.response.ProductResponse;
import io.taylor.wantedpreonboardingchallengebackend20.controller.product.request.ProductRequest;
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
    public ResponseEntity<Object> createProduct(AuthenticatedMember authenticatedMember,
                                                @RequestBody ProductRequest request) {
        productService.createProduct(authenticatedMember, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
    public ResponseEntity<Object> createOrderForProduct(AuthenticatedMember authenticatedMember,
                                                        @PathVariable("productId") Long productId,
                                                        @RequestBody ProductOrderRequest request) {
        productService.createOrderForProduct(authenticatedMember, productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}