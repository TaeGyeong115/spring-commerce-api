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

// to-be 응답 생성 service 로 옮기기 -> 버전닝 할 경우 controller 에서 응답값을 생성하는게 문제가 됨
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
        if (productService.buyProduct(memberData.memberId(), Long.parseLong(productId), price)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

//    @PatchMapping("/{productId}/approve")
//    public ResponseEntity<Object> approveProduct(@RequestHeader("Authorization") String authorization, @PathVariable("productId") String productId) {
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

//    @GetMapping("/reserved")
//    public ResponseEntity<Object> reservedProduct(@RequestHeader("Authorization") String authorization) {
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

//    @GetMapping("/purchased")
//    public ResponseEntity<Object> purchasedProduct(@RequestHeader("Authorization") String authorization) {
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
}
