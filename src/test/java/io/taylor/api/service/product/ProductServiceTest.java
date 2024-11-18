package io.taylor.api.service.product;

import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.product.request.ProductRequest;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.api.service.IntegrationTestSupport;
import io.taylor.api.service.product.request.ProductCreateServiceRequest;
import io.taylor.domain.product.Product;
import io.taylor.domain.product.ProductStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@Transactional
class ProductServiceTest extends IntegrationTestSupport {

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        // given
        int user1 = 1;
        int user2 = 2;

        product1 = createProduct(user1, "TV", 2_000_000, 10);
        product2 = createProduct(user1, "Video", 1_500_000, 5);
        product3 = createProduct(user2, "Radio", 3_000_000, 7);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("새로운 제품을 추가한다.")
    void createProduct() {
        //given
        ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
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
        assertThat(response.id()).isNotNull();
        assertThat(response.modifiedDateTime()).isNotNull();
        assertThat(response.createdDateTime()).isNotNull();
    }

    @Test
    @DisplayName("전체 제품 리스트를 조회한다.")
    void getAllProducts() {
        // when
        List<ProductResponse> response = productService.findAllProducts();

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(3)
                .extracting("name", "quantity", "status")
                .containsExactlyInAnyOrder(
                        tuple(product1.getName(), product1.remainingQuantity(), product1.getStatus()),
                        tuple(product2.getName(), product2.remainingQuantity(), product2.getStatus()),
                        tuple(product3.getName(), product3.remainingQuantity(), product3.getStatus())
                );

//        assertThat(response.get(0).price()).isEqualByComparingTo("2000000");
//        assertThat(response.get(1).price()).isEqualByComparingTo("1500000");
//        assertThat(response.get(2).price()).isEqualByComparingTo("3000000");
    }

    @Test
    @DisplayName("특정 제품 정보를 조회한다.")
    void getProduct() {
        // given
        Product product = createProduct(1L, "TV", 2_000_000, 1);
        productRepository.save(product);

        // when
        ProductResponse response = productService.findProductById(product.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("name", "quantity").contains("TV", 1);
        assertThat(response.price()).isEqualByComparingTo(String.valueOf(2_000_000));
    }

    private AuthenticatedMember createAuthenticatedMember(Long id, String email, String nickName) {
        return AuthenticatedMember.builder()
                .memberId(id)
                .email(email)
                .nickName(nickName)
                .build();
    }

    private Product createProduct(long providerId, String name, int price, int quantity) {
        return Product.builder()
                .providerId(providerId)
                .name(name)
                .price(new BigDecimal(price))
                .totalQuantity(quantity)
                .build();
    }

    private ProductRequest createProductRequest(String name, int price, int quantity) {
        return ProductRequest.builder()
                .name(name)
                .price(new BigDecimal(price))
                .quantity(quantity)
                .build();
    }

//    @Test
//    @DisplayName("회원이 제품을 주문한다.")
//    void saveOrderForProduct() {
//        // given
//        OrderServiceRequest request = OrderServiceRequest.builder()
//                .productId(order1.getProductId())
//                .quantity(order1.getQuantity())
//                .price(order1.getPrice())
//                .build();
//
//        // when
//        orderService.saveOrderForProduct(order1.getCustomerId(), request);
//
//        // then
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new AssertionError("Order not found"));
//        assertThat(order).isNotNull();
//        assertThat(order).extracting("productId", "customerId", "quantity")
//                .containsExactlyInAnyOrder(order1.getProductId(), order1.getCustomerId(), order1.getQuantity());
//        assertThat(order.getPrice()).isEqualByComparingTo(order1.getPrice());
//        assertThat(order.getTotalPrice()).isEqualByComparingTo(order1.getTotalPrice());
//    }
//
//    @Test
//    @DisplayName("회원은 수량이 부족하면 제품을 주문에 실패한다.")
//    void saveOrderForProduct_SoldOut() {
//        // given
//        OrderServiceRequest request = OrderServiceRequest.builder()
//                .productId(product1.getId())
//                .quantity(product1.remainingQuantity() + 1)
//                .price(product1.getPrice())
//                .build();
//
//        // when & then
//        assertThatThrownBy(() ->
//                orderService.saveOrderForProduct(1, request))
//                .isInstanceOf(ResponseStatusException.class)
//                .extracting(
//                        exception -> ((ResponseStatusException) exception).getStatusCode(),
//                        exception -> ((ResponseStatusException) exception).getReason())
//                .containsExactly(HttpStatus.BAD_REQUEST, "주문이 존재하지 않습니다.");
//    }

//    @Test
//    @DisplayName("회원은 구매 가격과 판매 가격이 다르면 주문에 실패한다.")
//    void saveOrderForProduct_DiffPrice() {
//        // when
//        List<OrderResponse> responses = orderService.saveOrderForProduct(1);
//
//        // then
//        assertThat(responses).hasSize(2).extracting("name", "quantity", "status")
//                .containsExactlyInAnyOrder(
//                        tuple(product1.getName(), order1.getQuantity(), order1.getStatus()),
//                        tuple(product2.getName(), order2.getQuantity(), order2.getStatus())
//                );
//        assertOrderResponse(responses.get(0), order1);
//        assertOrderResponse(responses.get(1), order2);
//    }

//    private OrderProductRequest saveProductOrderRequest(int price, int quantity) {
//        return OrderProductRequest.builder()
//                .price(new BigDecimal(price))
//                .quantity(quantity)
//                .build();
//    }
}
