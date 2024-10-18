//package io.taylor.wantedpreonboardingchallengebackend20.api.service;
//
//import io.taylor.wantedpreonboardingchallengebackend20.domain.member.Member;
//import io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderRepository;
//import io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderStatus;
//import io.taylor.wantedpreonboardingchallengebackend20.api.controller.order.response.OrderResponse;
//import io.taylor.wantedpreonboardingchallengebackend20.api.service.order.OrderService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderStatus.ORDER_IN_PROGRESS;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.tuple;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class OrderServiceTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    @Test
//    @DisplayName("회원의 주문 목록을 조회한다.")
//    void getAllOrderByMemberId() {
//        // given
//        LocalDateTime now = LocalDateTime.now();
//        Member member = createMember(1L, "test@test.com", "password", "member1", "member1");
//        List<OrderResponse> response = List.of(
//                createOrderResponse(1L, "TV", 1, 10000, 10000, ORDER_IN_PROGRESS, now),
//                createOrderResponse(2L, "Video", 2, 20000, 40000, ORDER_IN_PROGRESS, now)
//        );
//
//        given(orderRepository.findAllByCustomerId(member.getId())).willReturn(response);
//
//        // when
//        List<OrderResponse> orders = orderService.getOrderByMemberId(member.getId());
//
//        // then
//        assertThat(orders).isNotNull();
//        assertThat(orders).hasSize(2)
//                .extracting("id", "name", "quantity", "price", "totalPrice", "status", "modifiedDate", "createdDate")
//                .containsExactlyInAnyOrder(
//                        tuple(1L, "TV", 1, BigDecimal.valueOf(10000), BigDecimal.valueOf(10000), ORDER_IN_PROGRESS, now, now),
//                        tuple(2L, "Video", 2, BigDecimal.valueOf(20000), BigDecimal.valueOf(40000), ORDER_IN_PROGRESS, now, now)
//                );
//    }
//
//    @Test
//    @DisplayName("특정 주문을 조회한다.")
//    void getOrderById() {
//        // given
//        LocalDateTime now = LocalDateTime.now();
//        Member member = createMember(1L, "test@test.com", "password", "member1", "member1");
//        OrderResponse orderResponse = createOrderResponse(1L, "Video", 1, 10000, 10000, ORDER_IN_PROGRESS, now);
//
//        given(orderRepository.findById(1L, 1L)).willReturn(orderResponse);
//
//        // when
//        OrderResponse response = orderService.getOrderById(member.getId(), 1L);
//
//        //then
//        assertThat(response).isNotNull();
//        assertThat(response).extracting("id", "name", "quantity", "price", "totalPrice", "status", "modifiedDate", "createdDate")
//                .contains(1L, "Video", 1, BigDecimal.valueOf(10000), BigDecimal.valueOf(10000), ORDER_IN_PROGRESS, now, now);
//    }
//
//    @Test
//    @DisplayName("주문 상태를 변경한다.")
//    void updateOrderStatus() {
//        // given
//
//
//        // when
//
//
//        //then
//
//    }
//
//    private Member createMember(Long id, String email, String password, String name, String nickName) {
//        return Member.builder()
//                .email(email)
//                .password(password)
//                .name(name)
//                .nickName(nickName)
//                .build();
//    }
//
//    private OrderResponse createOrderResponse(Long id, String name, int quantity, int price, int totalPrice, OrderStatus status, LocalDateTime now) {
//        return OrderResponse.of(id, name, quantity, BigDecimal.valueOf(price), BigDecimal.valueOf(totalPrice), status, now, now);
//    }
//}