package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.ProductRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.ProductResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Product;
import io.taylor.wantedpreonboardingchallengebackend20.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[Success] 제품 등록 테스트")
    void createProductTest() {
        // given
        final long MEMBER_ID = 1L;
        final String EMAIL = "test@test.com";
        final String NICK_NAME = "test";
        final String PRODUCT_NAME = "냉장고";
        final long PRODUCT_PRICE = 20000000L;
        final long PRODUCT_QUANTITY = 2L;

        AuthenticatedMember member = new AuthenticatedMember(MEMBER_ID, EMAIL, NICK_NAME);
        ProductRequest request = new ProductRequest(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_QUANTITY);
        Product product = new Product(1L, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_QUANTITY);

        given(productRepository.save(any(Product.class))).willReturn(product);
        given(productRepository.findById(product.getId())).willReturn(product);

        // when
        ProductResponse createProduct = productService.createProduct(member, request);
        ProductResponse findProduct = productService.findProductById(createProduct.id());

        // then
        assertThat(findProduct).isNotNull();
        assertThat(findProduct.name()).isEqualTo(request.name());
        assertThat(findProduct.price()).isEqualTo(request.price());
        assertThat(findProduct.quantity()).isEqualTo(request.quantity());
    }

    @Test
    @DisplayName("[Success] 제품 리스트 조회")
    void getAllProductsTest() {
        // given
        List<Product> products = Arrays.asList(
                new Product(1L, "냉장고", 2000000L, 10),
                new Product(2L, "세탁기", 1500000L, 5),
                new Product(3L, "에어컨", 3000000L, 7)
        );

        given(productRepository.findAll()).willReturn(products);

        // when
        List<Product> allProducts = productService.findAllProducts();

        // then
        assertThat(allProducts).isNotNull();
        assertThat(allProducts).hasSize(3);
        assertThat(allProducts.get(0).getName()).isEqualTo(products.get(0).getName());
        assertThat(allProducts.get(1).getName()).isEqualTo(products.get(1).getName());
        assertThat(allProducts.get(2).getName()).isEqualTo(products.get(2).getName());
    }

    @Test
    @DisplayName("[Success] 특정 제품 조회")
    void getProductTest() {
        // given
        Product product = new Product(1L, "냉장고", 2000000L, 10);

        given(productRepository.findById(product.getId())).willReturn(product);

        // when
        ProductResponse foundProduct = productService.findProductById(product.getId());

        // then
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.id()).isEqualTo(product.getId());
        assertThat(foundProduct.name()).isEqualTo(product.getName());
        assertThat(foundProduct.price()).isEqualTo(product.getPrice());
        assertThat(foundProduct.quantity()).isEqualTo(product.getQuantity());
    }
}