package io.taylor.api.service.product;

import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.api.service.product.request.ProductCreateServiceRequest;
import io.taylor.domain.product.Product;
import io.taylor.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> findAllProducts() {
        List<Product> productList = productRepository.findAll();

        if (productList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 상품이 없습니다.");
        }

        return productList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void createProduct(AuthenticatedMember member, ProductCreateServiceRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .providerId(member.memberId())
                .price(request.price())
                .totalQuantity(request.quantity())
                .build();

        productRepository.save(product);
    }

    public ProductResponse findProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다."));

        return convertToResponse(product);
    }

    private ProductResponse convertToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .quantity(product.remainingQuantity())
                .price(product.getPrice())
                .status(product.getStatus())
                .modifiedDate(product.getModifiedDateTime())
                .createdDate(product.getCreatedDateTime())
                .build();
    }
}
