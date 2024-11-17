package io.taylor.api.controller.product;

import io.taylor.ControllerTestSupport;
import io.taylor.api.controller.product.request.ProductRequest;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.domain.product.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void getAllProducts() throws Exception {
        // given
        List<ProductResponse> products = List.of();
        given(productService.findAllProducts()).willReturn(products);

        // when & then
        mockMvc.perform(
                        get("/api/products")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("특정 상품을 조회한다.")
    void getProduct() throws Exception {
        // given
        ProductResponse response = createProduct(1L, "TV", 100_000, ProductStatus.FOR_SALE, 1);
        given(productService.findProductById(1L)).willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/products/1")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.status").value(response.status().name()))
                .andExpect(jsonPath("$.price").value(response.price()))
                .andExpect(jsonPath("$.quantity").value(response.quantity()));
    }

    @Test
    @DisplayName("상품을 등록한다.")
    void saveProduct() throws Exception {
        // given
        ProductRequest request = ProductRequest.builder()
                .name("TV")
                .price(new BigDecimal("100000"))
                .quantity(1)
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품 등록시 상품명은 필수 값이다.")
    void saveProductWithoutName() throws Exception {
        // given
        ProductRequest request = ProductRequest.builder()
                .price(new BigDecimal("100000"))
                .quantity(1)
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultMsg").value("상품명은 필수 항목입니다."));
    }

    @Test
    @DisplayName("상품 등록시 가격은 필수 값이다.")
    void saveProductWithoutPrice() throws Exception {
        // given
        ProductRequest request = ProductRequest.builder()
                .name("TV")
                .quantity(1)
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultMsg").value("가격은 필수 항목입니다."));
    }

    @Test
    @DisplayName("상품을 등록시 수량은 필수 값이다.")
    void saveProductWithoutQuantity() throws Exception {
        // given
        ProductRequest request = ProductRequest.builder()
                .name("TV")
                .price(new BigDecimal("100000"))
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultMsg").value("수량은 필수 항목입니다."));
    }

//    @Test
//    @DisplayName("상품을 주문한다.")
//    void orderProduct() throws Exception {
//        // given
//        OrderProductRequest request = OrderProductRequest.builder()
//                .productId(1L)
//                .price(new BigDecimal("100000"))
//                .quantity(1)
//                .build();
//        given(productService.orderProduct(1L)).willReturn();
//
//        // when & then
//        mockMvc.perform(
//                        post("/api/products/1")
//                                .content(objectMapper.writeValueAsString(request))
//                                .contentType(MediaType.APPLICATION_JSON)
//                ).andDo(print())
//                .andExpect(status().isCreated());
//    }

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