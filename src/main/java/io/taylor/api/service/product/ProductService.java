package io.taylor.api.service.product;

import io.taylor.api.controller.order.request.OrderStatusRequest;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.controller.product.response.OwnedProductResponse;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.api.service.log.LogService;
import io.taylor.api.service.order.OrderService;
import io.taylor.api.service.order.request.OrderServiceRequest;
import io.taylor.api.service.product.request.ProductServiceRequest;
import io.taylor.domain.log.ActionType;
import io.taylor.domain.log.TargetType;
import io.taylor.domain.order.OrderStatus;
import io.taylor.domain.product.Product;
import io.taylor.domain.product.ProductRepository;
import io.taylor.domain.product.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final LogService logService;

    public List<ProductResponse> findAllProducts() {
        List<Product> productList = productRepository.findAll();

        if (productList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 제품이 없습니다.");
        }

        return productList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse saveProduct(long memberId, ProductServiceRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .providerId(memberId)
                .price(request.price())
                .totalQuantity(request.quantity())
                .build();

        Product response = productRepository.save(product);
        logService.saveLog(ActionType.CREATE, TargetType.PRODUCT, memberId, product.getId());
        return convertToResponse(response);
    }

    public ProductResponse updateProduct(Long memberId, Long productId, ProductServiceRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 제품이 존재하지 않습니다."));

        isAuthorized(memberId, product.getProviderId());
        product.updateProduct(request.name(), request.price(), request.quantity());
        productRepository.save(product);
        ProductResponse response = convertToResponse(product);
        logService.saveLog(ActionType.UPDATE, TargetType.PRODUCT, memberId, productId);
        return response;
    }

    public OrderResponse orderProduct(long memberId, long productId, OrderServiceRequest request) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 제품이 존재하지 않습니다.");
        }

        Product product = optionalProduct.get();
        validateOrder(product, request);
        product.processSale(request.quantity());
        productRepository.save(product);
        return orderService.saveOrderForProduct(memberId, productId, request, product.getName());
    }

    public ProductResponse findProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 제품이 존재하지 않습니다."));

        return convertToResponse(product);
    }

    public List<OwnedProductResponse> findOwnedProducts(long memberId) {
        List<Product> productList = productRepository.findByProviderId(memberId);

        return productList.stream()
                .map(product -> OwnedProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .soldQuantity(product.getSoldQuantity())
                        .totalQuantity(product.getTotalQuantity())
                        .price(product.getPrice())
                        .status(product.getStatus())
                        .modifiedDateTime(product.getModifiedDateTime())
                        .createdDateTime(product.getCreatedDateTime())
                        .build())
                .collect(Collectors.toList());
    }

    public List<OrderResponse> findOrderByProductIdAndProviderId(Long productId, long memberId) {
        return orderService.findOrderByProductIdAndProviderId(productId, memberId);
    }

    public void updateProductStatus(long memberId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 제품이 존재하지 않습니다."));

        isAuthorized(memberId, product.getProviderId());
        if (product.getStatus() == ProductStatus.FOR_SALE) {
            product.setStatus(ProductStatus.SOLD_OUT);
            logService.saveLog(ActionType.DEACTIVATE, TargetType.PRODUCT, memberId, product.getId());
        } else if (product.getStatus() == ProductStatus.SOLD_OUT) {
            product.setStatus(ProductStatus.FOR_SALE);
            logService.saveLog(ActionType.ACTIVATE, TargetType.PRODUCT, memberId, product.getId());
        }

        productRepository.save(product);
    }

    public void updateOrderStatus(long memberId, long productId, OrderStatusRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 제품이 존재하지 않습니다."));

        isAuthorized(memberId, product.getProviderId());
        orderService.updateOrderStatus(request.status(), request.orderId());

        ActionType actionType = ActionType.COMPLETE;
        if (request.status().equals(OrderStatus.CANCELED)) {
            actionType = ActionType.DELETE;
        }
        logService.saveLog(actionType, TargetType.ORDER, memberId, request.orderId());
    }

    private static void isAuthorized(Long userId, Long targetId) {
        if (!Objects.equals(userId, targetId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
        }
    }

    private void validateOrder(Product product, OrderServiceRequest request) {
        if (product.getStatus() == ProductStatus.SOLD_OUT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "판매 중인 제품이 아닙니다.");
        }

        if (product.getPrice().compareTo(request.price()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "판매 금액 변동이 발생했습니다.");
        }
    }

    private ProductResponse convertToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .quantity(product.remainingQuantity())
                .price(product.getPrice())
                .status(product.getStatus())
                .modifiedDateTime(product.getModifiedDateTime())
                .createdDateTime(product.getCreatedDateTime())
                .build();
    }
}
