package io.taylor.wantedpreonboardingchallengebackend20.controller;

import io.taylor.wantedpreonboardingchallengebackend20.entity.Product;
import io.taylor.wantedpreonboardingchallengebackend20.model.request.MemberData;
import io.taylor.wantedpreonboardingchallengebackend20.model.request.ProductRequest;
import io.taylor.wantedpreonboardingchallengebackend20.model.response.ProductResponse;
import io.taylor.wantedpreonboardingchallengebackend20.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getProductList() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts());
    }

    @PostMapping("")
    public ResponseEntity<ProductResponse> postProduct(MemberData memberData, @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.postProduct(memberData, request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") String productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(Long.parseLong(productId)));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ProductResponse> purchaseProduct(MemberData memberData, @PathVariable("productId") String productId, @RequestBody long price) {
        productService.buyProduct(memberData.memberId(), Long.parseLong(productId), price);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{productId}/approve")
    public ResponseEntity<Object> approveProduct(MemberData memberData, @PathVariable("productId") String productId) {
        productService.approveProduct(memberData.memberId(), Long.parseLong(productId));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    @GetMapping("/reserved")
//    public ResponseEntity<Object> reservedProduct(MemberData memberData) {
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

//    @GetMapping("/purchased")
//    public ResponseEntity<Object> purchasedProduct(MemberData memberData) {
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
}
