package io.taylor.api.service.product;

import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.order.request.OrderStatusRequest;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.controller.product.response.OwnedProductResponse;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.api.service.log.LogService;
import io.taylor.api.service.order.OrderService;
import io.taylor.api.service.order.request.OrderServiceRequest;
import io.taylor.api.service.product.request.ProductCreateServiceRequest;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 상품이 없습니다.");
        }

        return productList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void saveProduct(AuthenticatedMember member, ProductCreateServiceRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .providerId(member.memberId())
                .price(request.price())
                .totalQuantity(request.quantity())
                .build();

        productRepository.save(product);
        logService.saveLog(ActionType.CREATE, TargetType.PRODUCT, member.memberId(), product.getId());
    }

    public void orderProduct(AuthenticatedMember member, OrderServiceRequest request) {
        Optional<Product> optionalProduct = productRepository.findById(request.productId());
        if (optionalProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다.");
        }

        Product product = optionalProduct.get();
        validateOrder(product, request);
        product.processSale(request.quantity());
        productRepository.save(product);
        orderService.saveOrderForProduct(member.memberId(), request);
    }

    public ProductResponse findProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다."));

        return convertToResponse(product);
    }

    public List<OwnedProductResponse> findOwnedProducts(AuthenticatedMember authenticatedMember) {
        List<Product> productList = productRepository.findByProviderId(authenticatedMember.memberId());

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

    public List<OrderResponse> findByProductIdAndProviderId(AuthenticatedMember member, Long productId) {
        return orderService.findByProductIdAndProviderId(productId, member.memberId());
    }

    public void updateProductStatus(AuthenticatedMember member, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다."));

        isAuthorized(member.memberId(), product.getProviderId());
        if (product.getStatus() == ProductStatus.FOR_SALE) {
            product.setStatus(ProductStatus.SOLD_OUT);
            logService.saveLog(ActionType.DEACTIVATE, TargetType.PRODUCT, member.memberId(), product.getId());
        } else if (product.getStatus() == ProductStatus.SOLD_OUT) {
            product.setStatus(ProductStatus.FOR_SALE);
            logService.saveLog(ActionType.ACTIVATE, TargetType.PRODUCT, member.memberId(), product.getId());
        }

        productRepository.save(product);
    }

    public void updateOrderStatus(AuthenticatedMember member, long productId, OrderStatusRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다."));

        isAuthorized(member.memberId(), product.getProviderId());
        orderService.updateOrderStatus(request.status(), request.orderId());

        ActionType actionType = ActionType.COMPLETE;
        if (request.status().equals(OrderStatus.CANCELED)) {
            actionType = ActionType.DELETE;
        }
        logService.saveLog(actionType, TargetType.ORDER, member.memberId(), request.orderId());
    }

    private static void isAuthorized(Long userId, Long targetId) {
        if (!Objects.equals(userId, targetId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
        }
    }

    private void validateOrder(Product product, OrderServiceRequest request) {
        if (product.getStatus() != ProductStatus.FOR_SALE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "판매하지 않는 상품입니다.");
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
