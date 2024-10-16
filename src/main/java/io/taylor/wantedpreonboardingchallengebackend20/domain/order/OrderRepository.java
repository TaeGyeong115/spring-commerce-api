package io.taylor.wantedpreonboardingchallengebackend20.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryQuery {
}
