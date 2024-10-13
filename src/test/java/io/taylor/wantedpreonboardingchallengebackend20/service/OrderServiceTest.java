package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.OrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Order;
import io.taylor.wantedpreonboardingchallengebackend20.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    @DisplayName("회원의 주문 리스트를 조회한다.")
    void getOrderById() {
        // given
        Order order1 = new Order(1L, member.memberId(), 10000L, 1);
        Order order2 = new Order(2L, member.memberId(), 20000L, 1);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        given(orderRepository.findAllByCustomerId(member.memberId())).willReturn(orders);

        // when
        List<OrderResponse> response = orderService.getOrderByMemberId(member.memberId());

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
//        assertThat(response.get(0)).isEqualTo(orders.get(0).getPrice());
//        assertThat(response.get(1)).isEqualTo(orders.get(1).getPrice());
    }

    @Test
    @DisplayName("")
    void test() {
        // given

        // when

        //then

    }
}