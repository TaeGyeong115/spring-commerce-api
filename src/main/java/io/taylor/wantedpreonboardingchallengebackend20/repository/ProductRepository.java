package io.taylor.wantedpreonboardingchallengebackend20.repository;

import io.taylor.wantedpreonboardingchallengebackend20.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product product);
    List<Product> findAll();
    Product findById(long id);
}
