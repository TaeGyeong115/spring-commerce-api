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

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<Product> findAllProducts() {
        List<Product> productList = productRepository.findAll();
        if (productList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 상품이 없습니다.");

        return productList;
    }

    public ProductResponse createProduct(AuthenticatedMember member, ProductRequest request) {
        try {
            Product product = productRepository.save(new Product(member.MemberId(), request.name(), request.price(), request.quantity()));

            return new ProductResponse(product.getId(), product.getName(), product.getQuantity(), product.getPrice(), product.getStatus(), product.getUpdatedAt(), product.getCreatedAt());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "상품 등록 실패");
        }
    }

    public ProductResponse findProductById(long productId) {
        try {
            Product product = productRepository.findById(productId);
            if (product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다.");

            return new ProductResponse(product.getId(), product.getName(), product.getQuantity(), product.getPrice(), product.getStatus(), product.getUpdatedAt(), product.getCreatedAt());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 상품이 없습니다.");
        }
    }

    public ProductResponse createOrderForProduct(AuthenticatedMember member, long productId, ProductOrderRequest productOrder) {

        Product product = productRepository.findById(productId);
        if (product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다.");

        try {
            Order order = new Order(productId, member.MemberId(), productOrder.price(), productOrder.quantity());
            orderRepository.save(order);
        } catch (IllegalArgumentException e) {
        }
        return null;
    }

    public ProductResponse findOrderForProduct(long productId) {
        return null;
    }
}
