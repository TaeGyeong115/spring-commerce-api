package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
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

    public List<Product> getProducts() {
        List<Product> productList = productRepository.findAll();
        if (productList.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 상품이 없습니다.");
        return productList;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = productRepository.save(new Product(request.name(), request.price(), request.quantity()));
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStatus(), product.getUpdatedAt(), product.getCreatedAt());
    }

    public ProductResponse getProductById(long productId) {
        try {
            Product product = productRepository.findById(productId);
            if (product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다.");
            return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStatus(), product.getUpdatedAt(), product.getCreatedAt());
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 상품이 없습니다.");
        }
    }

    public ProductResponse createOrderForProduct(long productId, ProductRequest product) {
        productRepository.findById(productId);
        if (product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다.");

        try {
            Order order = new Order(productId, 1, productId, product.price());
            orderRepository.save(order);
        } catch (Exception e) {
        }
        return null;
    }

    public ProductResponse getOrderForProduct(long productId) {
        return null;
    }
}
