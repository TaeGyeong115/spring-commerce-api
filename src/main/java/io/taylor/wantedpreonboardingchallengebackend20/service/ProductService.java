package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.ProductResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Order;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Product;
import io.taylor.wantedpreonboardingchallengebackend20.repository.OrderRepository;
import io.taylor.wantedpreonboardingchallengebackend20.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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
        List<ProductResponse> responsesList = new ArrayList<>();

        if (productList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 상품이 없습니다.");
        }

        for (Product entity : productList) {
            ProductResponse product = new ProductResponse(entity.getId(), entity.getName(), entity.remainingQuantity(), entity.getPrice(), entity.getStatus(), entity.getModifiedDate(), entity.getCreatedDate());
            responsesList.add(product);
        }

        return responsesList;
    }

    public ProductResponse createProduct(AuthenticatedMember member, ProductRequest request) {
        try {
            Product product = productRepository.save(new Product(member.memberId(), request.name(), request.price(), request.quantity()));
            ProductResponse response = new ProductResponse(product.getId(), product.getName(), product.getTotalQuantity(), product.getPrice(), product.getStatus(), product.getModifiedDate(), product.getCreatedDate());

            return response;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "상품 등록 실패");
        }
    }

    public ProductResponse findProductById(long productId) {
        try {
            Product product = productRepository.findById(productId);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다.");
            }

            return new ProductResponse(product.getId(), product.getName(), product.remainingQuantity(), product.getPrice(), product.getStatus(), product.getModifiedDate(), product.getCreatedDate());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "상품 조회 실패");
        }
    }

    public void createOrderForProduct(AuthenticatedMember member, long productId, ProductOrderRequest productOrder) {
        Product product = productRepository.findById(productId);

        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다.");
        }
        if (product.getProviderId() == member.memberId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "판매자는 본인의 상품을 구매할 수 없습니다.");
        }
        if (product.remainingQuantity() < productOrder.quantity()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "재고가 부족합니다.");
        }
        if (!product.getPrice().equals(productOrder.price())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "판매 금액 변동이 발생했습니다.");
        }

        try {
            product.processSale(productOrder.quantity());
            Order order = new Order(productId, member.memberId(), productOrder.price(), productOrder.quantity());
            orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "상품 주문 실패");
        }
    }

}
