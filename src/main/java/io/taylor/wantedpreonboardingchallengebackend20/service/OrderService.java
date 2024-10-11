package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedUser;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.OrderRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.OrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.ProductResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    public List<Order> getOrders() {
        return null;
    }

    public OrderResponse getOrderById(Long productId) {
        return null;
    }

    public ProductResponse updateOrder(AuthenticatedUser authenticatedUser, Long orderId, OrderRequest request) {
        return null;
    }
}
