package io.taylor.api.controller.product;

import io.taylor.api.controller.ControllerTestSupport;
import io.taylor.api.controller.order.request.OrderStatusRequest;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.controller.product.request.OrderProductRequest;
import io.taylor.api.controller.product.request.ProductRequest;
import io.taylor.api.controller.product.response.OwnedProductResponse;
import io.taylor.api.controller.product.response.ProductResponse;
import io.taylor.domain.order.OrderStatus;
import io.taylor.domain.product.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void getAllProducts() throws Exception {
        // given
        List<ProductResponse> response = List.of(createProductResponse(1L, "TV", 1000000, ProductStatus.FOR_SALE, 1));
        given(productService.findAllProducts()).willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/products")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(response.get(0).id()))
                .andExpect(jsonPath("$[0].name").value(response.get(0).name()))
                .andExpect(jsonPath("$[0].status").value(response.get(0).status().name()))
                .andExpect(jsonPath("$[0].price").value(response.get(0).price()))
                .andExpect(jsonPath("$[0].modifiedDateTime").hasJsonPath())
                .andExpect(jsonPath("$[0].createdDateTime").hasJsonPath());
    }

    @Test
    @DisplayName("특정 상품을 조회한다.")
    void getProduct() throws Exception {
        // given
        ProductResponse response = createProductResponse(1L, "TV", 100_000, ProductStatus.FOR_SALE, 1);
        given(productService.findProductById(1L)).willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/products/1")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.status").value(response.status().name()))
                .andExpect(jsonPath("$.price").value(response.price()))
                .andExpect(jsonPath("$.quantity").value(response.quantity()))
                .andExpect(jsonPath("$.modifiedDateTime").hasJsonPath())
                .andExpect(jsonPath("$.createdDateTime").hasJsonPath());
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
                .andExpect(jsonPath("$.message").value("상품명은 필수 항목입니다."));
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
                .andExpect(jsonPath("$.message").value("가격은 필수 항목입니다."));
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
                .andExpect(jsonPath("$.message").value("수량은 필수 항목입니다."));
    }

    @Test
    @DisplayName("상품을 등록시 최소 가격은 100원 이상이다.")
    void saveProductMinimumPrice() throws Exception {
        // given
        ProductRequest request = ProductRequest.builder()
                .name("TV")
                .quantity(1)
                .price(new BigDecimal("99"))
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("최소 가격은 100원 이상입니다."));
    }

    @Test
    @DisplayName("상품을 등록시 최소 수량은 1 이상이다.")
    void saveProductMinimumQuantity() throws Exception {
        // given
        ProductRequest request = ProductRequest.builder()
                .name("TV")
                .quantity(0)
                .price(new BigDecimal("10000"))
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("최소 수량은 1개 이상입니다."));
    }

    @Test
    @DisplayName("상품을 주문한다.")
    void orderProduct() throws Exception {
        // given
        OrderProductRequest request = OrderProductRequest.builder()
                .price(new BigDecimal("100000"))
                .quantity(1)
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products/1")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품 주문시 가격은 필수 값이다.")
    void orderProductWithoutPrice() throws Exception {
        // given
        OrderProductRequest request = OrderProductRequest.builder()
                .quantity(1)
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products/1")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("가격은 필수 항목입니다."));
    }

    @Test
    @DisplayName("상품 주문시 수량은 필수 값이다.")
    void orderProductWithoutQuantity() throws Exception {
        // given
        OrderProductRequest request = OrderProductRequest.builder()
                .price(new BigDecimal("100000"))
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products/1")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("수량은 필수 항목입니다."));
    }

    @Test
    @DisplayName("상품 주문시 최소 가격은 100원 이상이다.")
    void orderProductMinimumPrice() throws Exception {
        // given
        OrderProductRequest request = OrderProductRequest.builder()
                .quantity(1)
                .price(new BigDecimal("99"))
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products/1")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("최소 가격은 100원 이상입니다."));
    }

    @Test
    @DisplayName("상품 주문시 최소 수량은 1 이상이다.")
    void orderProductMinimumQuantity() throws Exception {
        // given
        OrderProductRequest request = OrderProductRequest.builder()
                .quantity(0)
                .price(new BigDecimal("100000"))
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/products/1")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("최소 수량은 1개 이상입니다."));
    }

    @Test
    @DisplayName("등록한 상품 목록을 조회한다.")
    void getAllOwnProducts() throws Exception {
        // given
        List<OwnedProductResponse> response = List.of(OwnedProductResponse.builder()
                .id(1L)
                .name("TV")
                .soldQuantity(1)
                .totalQuantity(1)
                .status(ProductStatus.FOR_SALE)
                .price(new BigDecimal(100000))
                .build());
        given(productService.findOwnedProducts(createAuthMember())).willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/products/owned")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(response.get(0).id()))
                .andExpect(jsonPath("$[0].name").value(response.get(0).name()))
                .andExpect(jsonPath("$[0].soldQuantity").value(response.get(0).soldQuantity()))
                .andExpect(jsonPath("$[0].totalQuantity").value(response.get(0).totalQuantity()))
                .andExpect(jsonPath("$[0].status").value(response.get(0).status().name()))
                .andExpect(jsonPath("$[0].price").value(response.get(0).price()))
                .andExpect(jsonPath("$[0].modifiedDateTime").hasJsonPath())
                .andExpect(jsonPath("$[0].createdDateTime").hasJsonPath());
    }

    @Test
    @DisplayName("등록한 특정 상품 주문 목록을 조회한다.")
    void getOwnProductOrder() throws Exception {
        // given
        List<OrderResponse> response = List.of(OrderResponse.builder()
                .name("TV")
                .quantity(1)
                .price(new BigDecimal(100000))
                .totalPrice(new BigDecimal(100000))
                .status(OrderStatus.IN_PROGRESS)
                .build());
        given(productService.findByProductIdAndProviderId(createAuthMember(), 1L)).willReturn(response);

        // when & then
        mockMvc.perform(
                        get("/api/products/owned/1")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(response.get(0).id()))
                .andExpect(jsonPath("$[0].name").value(response.get(0).name()))
                .andExpect(jsonPath("$[0].quantity").value(response.get(0).quantity()))
                .andExpect(jsonPath("$[0].price").value(response.get(0).price()))
                .andExpect(jsonPath("$[0].totalPrice").value(response.get(0).totalPrice()))
                .andExpect(jsonPath("$[0].status").value(response.get(0).status().name()))
                .andExpect(jsonPath("$[0].modifiedDateTime").hasJsonPath())
                .andExpect(jsonPath("$[0].createdDateTime").hasJsonPath());
    }

    @Test
    @DisplayName("특정 상품의 상태를 판매중 <-> 품절로 스위칭한다.")
    void updateProductStatus() throws Exception {
        // when & then
        mockMvc.perform(
                        patch("/api/products/owned/1")
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품의 특정 주문 상태를 변경한다.")
    void updateProductOrderStatus() throws Exception {
        OrderStatusRequest request = OrderStatusRequest.builder()
                .orderId(1L)
                .status(OrderStatus.COMPLETED)
                .build();

        // when & then
        mockMvc.perform(
                        patch("/api/products/owned/1/order")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품의 특정 주문 상태 변경시 주문 아이디는 필수 항목이다..")
    void updateProductOrderStatusWithoutOrderId() throws Exception {
        OrderStatusRequest request = OrderStatusRequest.builder()
                .status(OrderStatus.COMPLETED)
                .build();

        // when & then
        mockMvc.perform(
                        patch("/api/products/owned/1/order")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("주문 아이디는 필수 항목입니다."));
    }

    @Test
    @DisplayName("상품의 특정 주문 상태 변경시 주문 상태는 필수 항목이다.")
    void updateProductOrderStatusWithoutStatus() throws Exception {
        OrderStatusRequest request = OrderStatusRequest.builder()
                .orderId(1L)
                .build();

        // when & then
        mockMvc.perform(
                        patch("/api/products/owned/1/order")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("주문 상태는 필수 항목입니다."));
    }

    private static ProductResponse createProductResponse(long id, String name, int price, ProductStatus status, int quantity) {
        return ProductResponse.builder()
                .id(id)
                .name(name)
                .price(new BigDecimal(price))
                .status(status)
                .quantity(quantity)
                .build();
    }

}