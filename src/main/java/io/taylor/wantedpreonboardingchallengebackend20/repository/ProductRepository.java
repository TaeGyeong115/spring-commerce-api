package io.taylor.wantedpreonboardingchallengebackend20.repository;

import io.taylor.wantedpreonboardingchallengebackend20.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findById(long id);
}
