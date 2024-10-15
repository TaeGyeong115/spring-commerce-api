package io.taylor.wantedpreonboardingchallengebackend20.entity;

import io.taylor.wantedpreonboardingchallengebackend20.dto.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@Getter
@EqualsAndHashCode(callSuper = true)
@Table(name = "products", indexes = {
        @Index(name = "products_idx_id", columnList = "id", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private long providerId;

    private int quantity;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public Product(long providerId, String name, int price, int quantity) {
        this.providerId = providerId;
        this.name = name;
        this.price = BigDecimal.valueOf(price);
        this.quantity = quantity;
        this.status = ProductStatus.FOR_SALE;
    }
}
