package io.taylor.api.service.product;

import io.taylor.IntegrationTestSupport;
import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.product.request.ProductOrderRequest;
import io.taylor.api.controller.product.request.ProductRequest;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.domain.product.Product;
import io.taylor.domain.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("새로운 제품을 추가한다.")
    void addProduct() {
        // given
        Product product = createProduct(1, "TV", 20000000, 2);
        productRepository.save(product);

        ProductRequest request = createProductRequest("TV", 20000000, 2);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");

        // when
        productService.createProduct(member, request);
        ProductResponse response = productService.findProductById(product.getId());

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
                createProduct(3, "TV", 2_000_000, 10),
                createProduct(3, "Video", 1_500_000, 5),
                createProduct(3, "Radio", 3_000_000, 7)
        );
        productRepository.saveAll(products);

        // when
        List<ProductResponse> response = productService.findAllProducts();

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(3)
                .extracting("name", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("TV", 10),
                        tuple("Video", 5),
                        tuple("Radio", 7)
                );
        assertThat(response.get(0).price()).isEqualByComparingTo("2000000");
        assertThat(response.get(1).price()).isEqualByComparingTo("1500000");
        assertThat(response.get(2).price()).isEqualByComparingTo("3000000");
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

    @Test
    @DisplayName("제품을 주문하면 재고가 주문수량 만큼 감소한다.")
    void addOrder() {
        // given
        Product product = createProduct(3, "냉장고", 2000000, 10);
        productRepository.save(product);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");
        ProductOrderRequest request = createProductOrderRequest(2000000, 1);

        // when
        productService.createOrderForProduct(member, product.getId(), request);
        ProductResponse response = productService.findProductById(product.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response).extracting("name", "quantity")
                .contains("냉장고", 9);
    }

    @Test
    @DisplayName("존재하지 않는 제품은 주문할 수 없다.")
    void addOrder_WhenNotFound() {
        // given
        ProductOrderRequest request = createProductOrderRequest(2000000, 2);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");

        // when & then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, 1L, request);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("본인이 등록한 제품은 주문할 수 없다.")
    void addOrder_WhenForbidden() {
        // given
        Product product = createProduct(1, "냉장고", 2000000, 10);
        productRepository.save(product);

        ProductOrderRequest request = createProductOrderRequest(2000000, 2);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");

        // when & then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, product.getId(), request);
        });
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    @DisplayName("재고가 부족한 제품은 주문할 수 없다.")
    void addOrder_WhenStockIsNotEnough() {
        // given
        Product product = createProduct(2, "냉장고", 2000000, 4);
        productRepository.save(product);

        ProductOrderRequest request = createProductOrderRequest(2000000, 5);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");

        // when & then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, product.getId(), request);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("주문한 시점의 금액과 현재 판매 금액이 달라 주문할 수 없다.")
    void addOrder_WhenPriceChanges() {
        // given
        Product product = createProduct(2, "냉장고", 2000000, 10);
        productRepository.save(product);
        ProductOrderRequest request = createProductOrderRequest(1000000, 5);
        AuthenticatedMember member = createAuthenticatedMember(1L, "test@test.com", "member1");

        // when & then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, product.getId(), request);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
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

    private ProductOrderRequest createProductOrderRequest(int price, int quantity) {
        return ProductOrderRequest.builder()
                .price(new BigDecimal(price))
                .quantity(quantity)
                .build();
    }
}
