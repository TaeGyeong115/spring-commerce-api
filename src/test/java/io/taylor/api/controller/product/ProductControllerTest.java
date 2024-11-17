package io.taylor.api.controller.product;

import io.taylor.ControllerTestSupport;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.domain.product.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("상품이 없을 경우 에러를 반환한다.")
    void getAllProduct_NotFound() throws Exception {
        // when & then
        mockMvc.perform(
                        get("/api/products")
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void getAllProducts() throws Exception {
        // given
        List<ProductResponse> products = List.of(createProduct(1L, "TV", 100_000, ProductStatus.FOR_SALE, 1));
        given(productService.findAllProducts()).willReturn(products);

        // when & then
        mockMvc.perform(
                        get("/api/products")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


    private static ProductResponse createProduct(long id, String name, int price, ProductStatus status, int quantity) {
        return ProductResponse.builder()
                .id(id)
                .name(name)
                .price(new BigDecimal(price))
                .status(status)
                .quantity(quantity)
                .build();
    }

}