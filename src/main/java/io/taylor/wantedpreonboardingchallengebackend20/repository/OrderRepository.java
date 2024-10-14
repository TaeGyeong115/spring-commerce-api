package io.taylor.wantedpreonboardingchallengebackend20.repository;

import io.taylor.wantedpreonboardingchallengebackend20.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomerId(long customerId);
}
