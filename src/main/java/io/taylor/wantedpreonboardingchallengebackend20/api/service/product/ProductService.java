package io.taylor.wantedpreonboardingchallengebackend20.api.service.product;

import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.request.ProductRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.response.ProductResponse;
import io.taylor.wantedpreonboardingchallengebackend20.domain.order.Order;
import io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderRepository;
import io.taylor.wantedpreonboardingchallengebackend20.domain.product.Product;
import io.taylor.wantedpreonboardingchallengebackend20.domain.product.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<ProductResponse> findAllProducts() {
        List<Product> productList = productRepository.findAll();

        if (productList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 상품이 없습니다.");
        }

        return productList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void createProduct(AuthenticatedMember member, ProductRequest request) {
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

    public void createOrderForProduct(AuthenticatedMember member, long productId, ProductOrderRequest productOrder) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다."));

        validateOrder(product, member, productOrder);

        product.processSale(productOrder.quantity());
        Order order = Order.builder()
                .productId(productId)
                .customerId(member.memberId())
                .price(productOrder.price())
                .quantity(productOrder.quantity())
                .build();

        orderRepository.save(order);
    }

    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.remainingQuantity(),
                product.getPrice(),
                product.getStatus(),
                product.getModifiedDateTime(),
                product.getCreatedDateTime()
        );
    }

    private void validateOrder(Product product, AuthenticatedMember member, ProductOrderRequest productOrder) {
        if (product.getProviderId() == member.memberId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "판매자는 본인의 상품을 구매할 수 없습니다.");
        }
        if (product.remainingQuantity() < productOrder.quantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "재고가 부족합니다.");
        }
        if (!product.getPrice().equals(productOrder.price())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "판매 금액 변동이 발생했습니다.");
        }
    }
}
