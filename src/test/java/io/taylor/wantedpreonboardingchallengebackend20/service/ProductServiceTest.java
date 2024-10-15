package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductOrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.ProductResponse;
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

import java.math.BigDecimal;
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
    @DisplayName("새로운 제품을 추가한다.")
    void createProduct() {
        // given
        final String PRODUCT_NAME = "냉장고";
        final int PRODUCT_PRICE = 20000000;
        final int PRODUCT_QUANTITY = 2;

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
        assertThat(response.price().compareTo(new BigDecimal("20000000"))).isEqualTo(0);
        assertThat(response.quantity()).isEqualTo(request.quantity());
    }

    @Test
    @DisplayName("전체 제품 리스트를 조회한다.")
    void getAllProducts() {
        // given
        List<Product> products = Arrays.asList(new Product(1L, "냉장고", 2000000, 10), new Product(2L, "세탁기", 1500000, 5), new Product(3L, "에어컨", 3000000, 7));

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
    @DisplayName("특정 제품 정보를 조회한다.")
    void getProduct() {
        // given
        Product product = new Product(1L, "냉장고", 2000000, 10);

        given(productRepository.findById(product.getId())).willReturn(product);

        // when
        ProductResponse response = productService.findProductById(product.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(product.getId());
        assertThat(response.name()).isEqualTo(product.getName());
        assertThat(response.price()).isEqualTo(product.getPrice());
        assertThat(response.quantity()).isEqualTo(product.remainingQuantity());
    }

    @Test
    @DisplayName("특정 제품 구매를 요청하면 주문 목록에 추가된다.")
    void createOrder() {
        // given
        Product product = new Product(2L, "냉장고", 2000000, 10);
        ProductOrderRequest request = new ProductOrderRequest(product.getPrice(), 2);

        given(productRepository.findById(product.getId())).willReturn(product);

        // when
        productService.createOrderForProduct(member, product.getId(), request);
        ProductResponse response = productService.findProductById(product.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.price()).isEqualTo(request.price());
        assertThat(response.quantity()).isEqualTo(product.getTotalQuantity() - request.quantity());
    }

    @Test
    @DisplayName("존재하지 않는 제품은 주문할 수 없다.")
    void createOrder_WhenNotFound() {
        // given
        ProductOrderRequest request = new ProductOrderRequest(BigDecimal.valueOf(2000000), 2);

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, 1L, request);
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("본인이 등록한 제품은 주문할 수 없다.")
    void createOrder_WhenForbidden() {
        // given
        Product product = new Product(member.memberId(), "냉장고", 2000000, 10);
        ProductOrderRequest request = new ProductOrderRequest(BigDecimal.valueOf(2000000), 2);

        given(productRepository.findById(product.getId())).willReturn(product);

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, product.getId(), request);
        });
        Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    @DisplayName("제고가 부족한 제품은 주문할 수 없다.")
    void createOrder_WhenStockIsNotEnough() {
        // given
        Product product = new Product(1L, "냉장고", 2000000, 1);
        ProductOrderRequest request = new ProductOrderRequest(BigDecimal.valueOf(2000000), 5);

        given(productRepository.findById(product.getId())).willReturn(product);

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, 1L, request);
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

    }

    @Test
    @DisplayName("주문하려던 시점의 금액과 현재 판매 금액이 달라 주문할 수 없다.")
    void createOrder_WhenPriceChanges() {
        // given
        Product product = new Product(1L, "냉장고", 2000000, 1);
        ProductOrderRequest request = new ProductOrderRequest(BigDecimal.valueOf(1000000), 5);

        given(productRepository.findById(product.getId())).willReturn(product);

        // when & then
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productService.createOrderForProduct(member, 1L, request);
        });
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

    }

}