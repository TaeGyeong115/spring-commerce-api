package io.taylor.wantedpreonboardingchallengebackend20.api.service;

import io.taylor.wantedpreonboardingchallengebackend20.domain.order.Order;
import io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderRepository;
import io.taylor.wantedpreonboardingchallengebackend20.domain.product.Product;
import io.taylor.wantedpreonboardingchallengebackend20.domain.product.ProductRepository;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.request.ProductRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.response.ProductResponse;
import io.taylor.wantedpreonboardingchallengebackend20.api.service.product.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("새로운 제품을 추가한다.")
    void addProduct() {
        // given
        ProductRequest request = createProductRequest("TV", 20000000, 2);
        Product product = createProduct(1L, 1, "TV", 20000000, 2);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        productService.createProduct(member, request);
        ProductResponse response = productService.findProductById(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("name", "price", "quantity")
                .contains("TV", BigDecimal.valueOf(20000000), 2);
    }

    @Test
    @DisplayName("전체 제품 리스트를 조회한다.")
    void getAllProducts() {
        // given
        List<Product> products = Arrays.asList(
                createProduct(1L, 1, "TV", 2000000, 10),
                createProduct(2L, 2, "Video", 1500000, 5),
                createProduct(3L, 3, "Radio", 3000000, 7)
        );

        given(productRepository.findAll()).willReturn(products);

        // when
        List<ProductResponse> response = productService.findAllProducts();

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(3)
                .extracting("id", "name", "price", "quantity")
                .containsExactlyInAnyOrder(
                        tuple(1L, "TV", BigDecimal.valueOf(2000000), 10),
                        tuple(2L, "Video", BigDecimal.valueOf(1500000), 5),
                        tuple(3L, "Radio", BigDecimal.valueOf(3000000), 7)
                );
    }

    @Test
    @DisplayName("특정 제품 정보를 조회한다.")
    void getProduct() {
        // given
        Product product = createProduct(1L, 1, "냉장고", 2000000, 10);

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // when
        ProductResponse response = productService.findProductById(product.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("name", "price", "quantity")
                .contains("냉장고", BigDecimal.valueOf(2000000), 10);
    }

    @Test
    @DisplayName("제품을 주문하면 재고가 주문수량 만큼 감소한다.")
    void addOrder() {
        // given
        Product product = createProduct(1L, 3, "냉장고", 2000000, 10);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");
        Order order = createOrder(1, 1, 2000000, 1);
        ProductOrderRequest request = createProductOrderRequest(2000000, 1);

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        productService.createOrderForProduct(member, 1, request);
        ProductResponse response = productService.findProductById(product.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("id", "name", "quantity", "price")
                .contains(1L, "냉장고", 9, BigDecimal.valueOf(2000000));
    }

    @Test
    @DisplayName("존재하지 않는 제품은 주문할 수 없다.")
    void addOrder_WhenNotFound() {
        // given
        ProductOrderRequest request = createProductOrderRequest(2000000, 2);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, 1L, request);
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("본인이 등록한 제품은 주문할 수 없다.")
    void addOrder_WhenForbidden() {
        // given
        Product product = createProduct(1L, 1, "냉장고", 2000000, 10);
        ProductOrderRequest request = createProductOrderRequest(2000000, 2);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, product.getId(), request);
        });
        Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    @DisplayName("재고가 부족한 제품은 주문할 수 없다.")
    void addOrder_WhenStockIsNotEnough() {
        // given
        Product product = createProduct(1L, 2, "냉장고", 2000000, 4);
        ProductOrderRequest request = createProductOrderRequest(2000000, 5);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, 1L, request);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("주문한 시점의 금액과 현재 판매 금액이 달라 주문할 수 없다.")
    void addOrder_WhenPriceChanges() {
        // given
        Product product = createProduct(1L, 2, "냉장고", 2000000, 10);
        ProductOrderRequest request = createProductOrderRequest(1000000, 5);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, 1L, request);
        });
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    private AuthenticatedMember createAuthenticatedMember(Long id, String email, String nickName) {
        return AuthenticatedMember.builder()
                .memberId(id)
                .email(email)
                .nickName(nickName)
                .build();
    }

    private Product createProduct(Long id, long providerId, String name, int price, int quantity) {
        return Product.builder()
                .id(id)
                .providerId(providerId)
                .name(name)
                .price(price)
                .totalQuantity(quantity)
                .build();
    }

    private Order createOrder(int productId, int customerId, int price, int quantity) {
        return Order.builder()
                .productId(productId)
                .customerId(customerId)
                .price(BigDecimal.valueOf(price))
                .quantity(quantity).build();
    }

    private ProductRequest createProductRequest(String name, int price, int quantity) {
        return new ProductRequest(name, price, quantity);
    }

    private ProductOrderRequest createProductOrderRequest(int price, int quantity) {
        return new ProductOrderRequest(BigDecimal.valueOf(price), quantity);
    }
}
