package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.OrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        List<OrderResponse> orders = new ArrayList<>();
        orders.add(new OrderResponse(1L, "product1", 1, 10000L, 10000L, 0, timestamp, timestamp));
        orders.add(new OrderResponse(2L, "product2", 2, 20000L, 40000L, 0, timestamp, timestamp));

        given(orderRepository.findAllByCustomerId(member.memberId())).willReturn(orders);

        // when
        List<OrderResponse> response = orderService.getOrderByMemberId(member.memberId());

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
        assertThat(response.get(0).price()).isEqualTo(orders.get(0).price());
        assertThat(response.get(1).price()).isEqualTo(orders.get(1).price());
        long totalPrice1 = response.get(0).price() * response.get(0).quantity();
        assertThat(totalPrice1).isEqualTo(orders.get(0).totalPrice());
        long totalPrice2 = response.get(1).price() * response.get(1).quantity();
        assertThat(totalPrice2).isEqualTo(orders.get(1).totalPrice());
    }

    @Test
    @DisplayName("특정 주문을 조회한다.")
    void getOrderById() {
        // given
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        OrderResponse order = new OrderResponse(1L, "product1", 1, 10000L, 10000L, 0, timestamp, timestamp);

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