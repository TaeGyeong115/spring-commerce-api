package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.ProductOrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.ProductResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Order;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Product;
import io.taylor.wantedpreonboardingchallengebackend20.repository.OrderRepository;
import io.taylor.wantedpreonboardingchallengebackend20.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class ProductServiceTest {

    private static AuthenticatedMember member;
    private static final long MEMBER_ID = 1L;
    private static final String EMAIL = "test@test.com";
    private static final String NICK_NAME = "test";

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new AuthenticatedMember(MEMBER_ID, EMAIL, NICK_NAME);
    }

    @Test
    @DisplayName("[Success] 제품 등록 테스트")
    void createProductTest() {
        // given
        final String PRODUCT_NAME = "냉장고";
        final long PRODUCT_PRICE = 20000000L;
        final long PRODUCT_QUANTITY = 2L;

        ProductRequest request = new ProductRequest(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_QUANTITY);
        Product product = new Product(1L, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_QUANTITY);

        given(productRepository.save(any(Product.class))).willReturn(product);
        given(productRepository.findById(product.getId())).willReturn(product);

        // when
        ProductResponse createProduct = productService.createProduct(member, request);
        ProductResponse response = productService.findProductById(createProduct.id());

        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.price()).isEqualTo(request.price());
        assertThat(response.quantity()).isEqualTo(request.quantity());
    }

    @Test
    @DisplayName("[Success] 제품 리스트 조회")
    void getAllProductsTest() {
        // given
        List<Product> products = Arrays.asList(new Product(1L, "냉장고", 2000000L, 10), new Product(2L, "세탁기", 1500000L, 5), new Product(3L, "에어컨", 3000000L, 7));

        given(productRepository.findAll()).willReturn(products);

        // when
        List<ProductResponse> response = productService.findAllProducts();

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(3);
        assertThat(response.get(0).name()).isEqualTo(products.get(0).getName());
        assertThat(response.get(1).name()).isEqualTo(products.get(1).getName());
        assertThat(response.get(2).name()).isEqualTo(products.get(2).getName());
    }

    @Test
    @DisplayName("[Success] 특정 제품 조회")
    void getProductTest() {
        // given
        Product product = new Product(1L, "냉장고", 2000000L, 10);

        given(productRepository.findById(product.getId())).willReturn(product);

        // when
        ProductResponse response = productService.findProductById(product.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(product.getId());
        assertThat(response.name()).isEqualTo(product.getName());
        assertThat(response.price()).isEqualTo(product.getPrice());
        assertThat(response.quantity()).isEqualTo(product.getQuantity());
    }

    @Test
    @DisplayName("[Success] 제품 구매 신청")
    void createOrderTest() {
        // given
        Product product = new Product(2L, "냉장고", 2000000L, 10);
        ProductOrderRequest request = new ProductOrderRequest(product.getPrice(), product.getQuantity());
        Order order = new Order(product.getId(), member.memberId(), product.getPrice(), product.getQuantity());

        given(productRepository.findById(product.getId())).willReturn(product);
        given(orderRepository.save(order)).willReturn(order);

        // when
        ProductOrderResponse response = productService.createOrderForProduct(member, product.getId(), request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.price()).isEqualTo(request.price());
        assertThat(response.quantity()).isEqualTo(request.quantity());
    }

    @Test
    @DisplayName("[NOT_FOUND] 존재하지 않는 제품")
    void createOrderTest_WhenNotFound() {
        // given
        ProductOrderRequest request = new ProductOrderRequest(2000000L, 2);

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, 1L, request);
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("[FORBIDDEN] 본인 제품 구매")
    void createOrderTest_WhenForbidden() {
        // given
        Product product = new Product(member.memberId(), "냉장고", 2000000L, 10);
        ProductOrderRequest request = new ProductOrderRequest(2000000L, 2);

        given(productRepository.findById(product.getId())).willReturn(product);

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, product.getId(), request);
        });
        Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    @DisplayName("[NOT_FOUND] 재고 부족")
    void createOrderTest_WhenStockIsNotEnough() {
        // given
        Product product = new Product(1L, "냉장고", 2000000L, 1);
        ProductOrderRequest request = new ProductOrderRequest(2000000L, 5);

        given(productRepository.findById(product.getId())).willReturn(product);

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, 1L, request);
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

    }

    @Test
    @DisplayName("[NOT_FOUND] 금액 변동")
    void createOrderTest_WhenPriceChanges() {
        // given
        Product product = new Product(1L, "냉장고", 2000000L, 1);
        ProductOrderRequest request = new ProductOrderRequest(1000000L, 5);

        given(productRepository.findById(product.getId())).willReturn(product);

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, 1L, request);
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

    }

}