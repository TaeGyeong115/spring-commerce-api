package io.taylor.api.service.order;

import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.order.request.OrderRequest;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.service.order.request.OrderServiceRequest;
import io.taylor.domain.order.Order;
import io.taylor.domain.order.OrderRepository;
import io.taylor.domain.product.Product;
import io.taylor.domain.product.ProductRepository;
import io.taylor.domain.product.ProductStatus;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public void createOrderForProduct(AuthenticatedMember member, OrderServiceRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품이 존재하지 않습니다."));

        validateOrder(product, member, request);

        product.processSale(request.quantity());
        productRepository.save(product);

        Order order = Order.builder()
                .productId(request.productId())
                .customerId(member.memberId())
                .price(request.price())
                .quantity(request.quantity())
                .build();

        orderRepository.save(order);
    }

    public List<OrderResponse> getOrderByMemberId(long memberId) {
        return orderRepository.findAllByCustomerId(memberId);
    }

    public OrderResponse getOrderById(long memberId, long orderId) {
        return orderRepository.findById(orderId, memberId);
    }

    public void updateOrder(AuthenticatedMember member, long orderId, OrderRequest request) {
    }

    private void validateOrder(Product product, AuthenticatedMember member, OrderServiceRequest request) {
        if (product.getProviderId() == member.memberId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "판매자는 본인의 상품을 구매할 수 없습니다.");
        }

        if (product.getStatus() != ProductStatus.FOR_SALE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "판매하지 않는 상품입니다.");
        }

        if (product.getPrice().compareTo(request.price()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "판매 금액 변동이 발생했습니다.");
        }
    }
}
