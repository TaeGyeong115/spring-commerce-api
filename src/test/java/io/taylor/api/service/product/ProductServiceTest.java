package io.taylor.api.service.product;

import io.taylor.IntegrationTestSupport;
import io.taylor.api.controller.order.request.OrderStatusRequest;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.controller.product.response.OwnedProductResponse;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.api.service.order.request.OrderServiceRequest;
import io.taylor.api.service.product.request.ProductServiceRequest;
import io.taylor.domain.order.Order;
import io.taylor.domain.order.OrderStatus;
import io.taylor.domain.product.Product;
import io.taylor.domain.product.ProductStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@Transactional
class ProductServiceTest extends IntegrationTestSupport {

    private final int user1 = 1;
    private final int user2 = 2;

    private Product product1;
    private Product product2;
    private Product product3;

    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        // given
        product1 = createProduct(user1, "TV", 2_000_000, 10);
        product2 = createProduct(user1, "Video", 1_500_000, 5);
        product3 = createProduct(user2, "Radio", 3_000_000, 7);
        productRepository.saveAll(List.of(product1, product2, product3));

        order1 = createOrder(product1.getId(), user1, product1.getPrice().intValue(), 1);
        order2 = createOrder(product1.getId(), user2, product1.getPrice().intValue(), 1);
        orderRepository.saveAll(List.of(order1, order2));
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("새로운 제품을 추가한다.")
    void createProduct() {
        //given
        ProductServiceRequest request = ProductServiceRequest.builder()
                .name("Computer")
                .price(new BigDecimal(20000000))
                .quantity(4)
                .build();

        // when
        ProductResponse response = productService.saveProduct(1, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("name", "price", "quantity", "status")
                .containsExactlyInAnyOrder(request.name(), request.price(), request.quantity(), ProductStatus.FOR_SALE);
        ProductResponseCheckNotNull(response);
    }

    @Test
    @DisplayName("제품 정보를 수정한다.")
    void updateProduct() {
        //given
        ProductServiceRequest request = ProductServiceRequest.builder()
                .name("Test")
                .price(new BigDecimal(20000000))
                .quantity(4)
                .build();

        // when
        ProductResponse response = productService.updateProduct(product1.getProviderId(), product1.getId(), request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("name", "price", "quantity", "status")
                .containsExactlyInAnyOrder(request.name(), request.price(), request.quantity(), ProductStatus.FOR_SALE);
        ProductResponseCheckNotNull(response);
    }

    @Test
    @DisplayName("전체 제품 리스트를 조회한다.")
    void getAllProducts() {
        // when
        List<ProductResponse> response = productService.findAllProducts();

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(3)
                .extracting("name", "quantity", "status", "price")
                .containsExactlyInAnyOrder(
                        tuple(product1.getName(), product1.getRemainingQuantity(), product1.getStatus(), product1.getPrice()),
                        tuple(product2.getName(), product2.getRemainingQuantity(), product2.getStatus(), product2.getPrice()),
                        tuple(product3.getName(), product3.getRemainingQuantity(), product3.getStatus(), product3.getPrice())
                );
        ProductResponseCheckNotNull(response.get(0));
        ProductResponseCheckNotNull(response.get(1));
        ProductResponseCheckNotNull(response.get(2));
    }

    @Test
    @DisplayName("특정 제품을 상세 조회한다.")
    void getProduct() {
        // given
        Product product = createProduct(1, "TV", 2_000_000, 1);
        productRepository.save(product);

        // when
        ProductResponse response = productService.findProductById(product.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("name", "price", "quantity", "status")
                .containsExactlyInAnyOrder(product.getName(), product.getPrice(), product.getRemainingQuantity(), product.getStatus());
        ProductResponseCheckNotNull(response);
    }

    @Test
    @DisplayName("소유 제품 리스트를 조회할 수 있다.")
    void getAllOwnProducts() {
        // when
        List<OwnedProductResponse> response = productService.findOwnedProducts(user1);

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
        assertThat(response).extracting("name", "soldQuantity", "totalQuantity", "price", "status")
                .containsExactlyInAnyOrder(
                        tuple(product1.getName(), product1.getSoldQuantity(), product1.getTotalQuantity(), product1.getPrice(), product1.getStatus()),
                        tuple(product2.getName(), product2.getSoldQuantity(), product2.getTotalQuantity(), product2.getPrice(), product2.getStatus())
                );
        OwnedProductResponseCheckNotNull(response.get(0));
        OwnedProductResponseCheckNotNull(response.get(1));
    }

    @Test
    @DisplayName("소유 제품의 주문 리스트를 조회할 수 있다.")
    void getOwnProductOrders() {
        // when
        List<OrderResponse> response = productService.findOrderByProductIdAndProviderId(product1.getId(), product1.getProviderId());

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
        assertThat(response).extracting("id", "name", "quantity", "status")
                .containsExactlyInAnyOrder(
                        tuple(order1.getId(), product1.getName(), order1.getQuantity(), order1.getStatus()),
                        tuple(order2.getId(), product1.getName(), order2.getQuantity(), order2.getStatus())
                );
        assertThatOrderResponse(response.get(0), order1);
        assertThatOrderResponse(response.get(1), order2);
    }

    @Test
    @DisplayName("제품을 주문할 수 있다.")
    void orderForProduct() {
        // given
        OrderServiceRequest request = OrderServiceRequest.builder()
                .quantity(product1.getRemainingQuantity())
                .price(product1.getPrice())
                .build();

        // when
        OrderResponse response = productService.orderProduct(order1.getCustomerId(), product1.getId(), request);

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("name", "quantity", "status")
                .containsExactlyInAnyOrder(product1.getName(), request.quantity(), order1.getStatus());
        assertThat(response.id()).isNotNull();
        assertThat(response.price()).isEqualByComparingTo(response.price());
        assertThat(response.totalPrice()).isEqualByComparingTo(request.price().multiply(new BigDecimal(request.quantity())));
        assertThat(response.modifiedDateTime()).isNotNull();
        assertThat(response.createdDateTime()).isNotNull();
    }

    @Test
    @DisplayName("제품 주문시, 제품 수량이 부족하면 주문에 실패한다.")
    void orderForProduct_QuantityLack() {
        // given
        OrderServiceRequest request = OrderServiceRequest.builder()
                .quantity(product1.getRemainingQuantity() + 1)
                .price(product1.getPrice())
                .build();

        // when & then
        assertThatThrownBy(() -> productService.orderProduct(order1.getCustomerId(), product1.getId(), request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(
                        exception -> ((ResponseStatusException) exception).getStatusCode(),
                        exception -> ((ResponseStatusException) exception).getReason())
                .containsExactly(HttpStatus.BAD_REQUEST, "재고가 부족합니다.");
    }

    @Test
    @DisplayName("제품 주문시, 요청 가격과 판매 가격이 다르면 주문에 실패한다.")
    void orderForProduct_DiffPrice() {
        // given
        OrderServiceRequest request = OrderServiceRequest.builder()
                .quantity(product1.getRemainingQuantity())
                .price(product1.getPrice().multiply(new BigDecimal(10)))
                .build();

        // when & then
        assertThatThrownBy(() -> productService.orderProduct(order1.getCustomerId(), product1.getId(), request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(
                        exception -> ((ResponseStatusException) exception).getStatusCode(),
                        exception -> ((ResponseStatusException) exception).getReason())
                .containsExactly(HttpStatus.BAD_REQUEST, "판매 금액 변동이 발생했습니다.");
    }

    @Test
    @DisplayName("제품 주문시, 제품 상태가 SOLD_OUT 이면 주문에 실패한다.")
    void orderForProduct_SoldOut() {
        // given
        OrderServiceRequest request = OrderServiceRequest.builder()
                .quantity(product1.getRemainingQuantity())
                .price(product1.getPrice())
                .build();
        product1.setStatus(ProductStatus.SOLD_OUT);

        // when & then
        assertThatThrownBy(() -> productService.orderProduct(order1.getCustomerId(), product1.getId(), request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(
                        exception -> ((ResponseStatusException) exception).getStatusCode(),
                        exception -> ((ResponseStatusException) exception).getReason())
                .containsExactly(HttpStatus.FORBIDDEN, "판매 중인 제품이 아닙니다.");
    }

    @Test
    @DisplayName("소유 제품의 판매 상태를 변경한다.")
    void updateProductStatus() {
        // when
        productService.updateProductStatus(order1.getCustomerId(), order1.getProductId());

        // then
        Product product = productRepository.findById(product1.getId()).orElseThrow(() -> new AssertionError("Product Not Found"));
        assertThat(product.getStatus()).isEqualTo(ProductStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("소유 제품의 주문을 완료 상태로 변경할 수 있다.")
    void updateProductOrderStatus_COMPLETED() {
        // given
        OrderStatusRequest request = OrderStatusRequest.builder()
                .orderId(order1.getId())
                .status(OrderStatus.COMPLETED)
                .build();

        // when
        productService.updateOrderStatus(order1.getCustomerId(), order1.getProductId(), request);

        // then
        Order order = orderRepository.findById(order1.getId()).orElseThrow(() -> new AssertionError("Order Not Found"));
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("제품의 주문을 취소 상태로 변경할 수 있다.")
    void updateProductOrderStatus_CANCELED() {
        // given
        OrderStatusRequest request = OrderStatusRequest.builder()
                .orderId(order1.getId())
                .status(OrderStatus.CANCELED)
                .build();

        // when
        productService.updateOrderStatus(order1.getCustomerId(), order1.getProductId(), request);

        // then
        Order order = orderRepository.findById(order1.getId()).orElseThrow(() -> new AssertionError("Order Not Found"));
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    @DisplayName("제품의 주문 상태가 완료 상태일 경우 변경할 수 없다.")
    void updateProductOrderStatusComplete_CANCELED() {
        // given
        OrderStatusRequest request = OrderStatusRequest.builder()
                .orderId(order1.getId())
                .status(OrderStatus.CANCELED)
                .build();
        order1.updateStatue(OrderStatus.COMPLETED);

        // when & then
        assertThatThrownBy(() ->
                productService.updateOrderStatus(order1.getCustomerId(), order1.getProductId(), request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(
                        exception -> ((ResponseStatusException) exception).getStatusCode(),
                        exception -> ((ResponseStatusException) exception).getReason())
                .containsExactly(HttpStatus.BAD_REQUEST, "이미 완료된 주문입니다.");
    }

    @Test
    @DisplayName("제품의 주문 상태가 취소일 경우 변경할 수 없다.")
    void updateProductOrderStatusCancel_CANCELED() {
        // given
        OrderStatusRequest request = OrderStatusRequest.builder()
                .orderId(order1.getId())
                .status(OrderStatus.CANCELED)
                .build();
        order1.updateStatue(OrderStatus.CANCELED);

        // when & then
        assertThatThrownBy(() ->
                productService.updateOrderStatus(order1.getCustomerId(), order1.getProductId(), request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(
                        exception -> ((ResponseStatusException) exception).getStatusCode(),
                        exception -> ((ResponseStatusException) exception).getReason())
                .containsExactly(HttpStatus.BAD_REQUEST, "이미 취소된 주문입니다.");
    }

    private void ProductResponseCheckNotNull(ProductResponse response) {
        assertThat(response.id()).isNotNull();
        assertThat(response.modifiedDateTime()).isNotNull();
        assertThat(response.createdDateTime()).isNotNull();
    }

    private void OwnedProductResponseCheckNotNull(OwnedProductResponse response) {
        assertThat(response.id()).isNotNull();
        assertThat(response.modifiedDateTime()).isNotNull();
        assertThat(response.createdDateTime()).isNotNull();
    }

    private void assertThatOrderResponse(OrderResponse response, Order order) {
        assertThat(response.price()).isEqualByComparingTo(order.getPrice());
        assertThat(response.totalPrice()).isEqualByComparingTo(order.getTotalPrice());
        assertThat(response.modifiedDateTime()).isNotNull();
        assertThat(response.createdDateTime()).isNotNull();
    }
}
