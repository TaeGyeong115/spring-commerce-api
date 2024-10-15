package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.OrderStatus;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.OrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class OrderServiceTest {

    private static AuthenticatedMember member;
    private static final long MEMBER_ID = 1L;
    private static final String EMAIL = "test@test.com";
    private static final String NICK_NAME = "test";

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new AuthenticatedMember(MEMBER_ID, EMAIL, NICK_NAME);
    }

    @Test
    @DisplayName("회원의 주문 목록을 조회한다.")
    void getAllOrderByMemberId() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<OrderResponse> orders = new ArrayList<>();
        orders.add(new OrderResponse(1L, "product1", 1, BigDecimal.valueOf(10000), BigDecimal.valueOf(10000), OrderStatus.ORDER_IN_PROGRESS, now, now));
        orders.add(new OrderResponse(2L, "product2", 2, BigDecimal.valueOf(20000), BigDecimal.valueOf(40000), OrderStatus.ORDER_IN_PROGRESS, now, now));

        given(orderRepository.findAllByCustomerId(member.memberId())).willReturn(orders);

        // when
        List<OrderResponse> response = orderService.getOrderByMemberId(member.memberId());

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
        assertThat(response.get(0).price()).isEqualTo(orders.get(0).price());
        assertThat(response.get(1).price()).isEqualTo(orders.get(1).price());
        BigDecimal totalPrice1 = response.get(0).price().multiply(BigDecimal.valueOf(response.get(0).quantity()));
        assertThat(totalPrice1).isEqualTo(orders.get(0).totalPrice());
        BigDecimal totalPrice2 = response.get(1).price().multiply(BigDecimal.valueOf(response.get(1).quantity()));
        assertThat(totalPrice2).isEqualTo(orders.get(1).totalPrice());
    }

    @Test
    @DisplayName("특정 주문을 조회한다.")
    void getOrderById() {
        // given
        LocalDateTime now = LocalDateTime.now();
        OrderResponse order = new OrderResponse(1L, "product1", 1, BigDecimal.valueOf(10000), BigDecimal.valueOf(10000), OrderStatus.ORDER_IN_PROGRESS, now, now);

        given(orderRepository.findById(member.memberId(), order.id())).willReturn(order);

        // when
        OrderResponse response = orderService.getOrderById(member.memberId(), order.id());

        //then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(order);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void updateOrderStatus() {
        // given

        // when


        //then

    }
}