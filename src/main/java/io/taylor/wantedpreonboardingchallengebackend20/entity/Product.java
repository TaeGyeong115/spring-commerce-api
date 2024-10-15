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

    private int totalQuantity;

    private int soldQuantity;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public Product(long providerId, String name, int price, int totalQuantity) {
        this.providerId = providerId;
        this.name = name;
        this.price = BigDecimal.valueOf(price);
        this.totalQuantity = totalQuantity;
        this.soldQuantity = 0;
        this.status = ProductStatus.FOR_SALE;
    }

    public int remainingQuantity() {
        return this.totalQuantity - this.soldQuantity;
    }

    public void increaseSoldQuantity(int quantity) {
        this.soldQuantity += quantity;
    }

    public void processSale(int quantity) {
        increaseSoldQuantity(quantity);

        if (this.totalQuantity == this.soldQuantity) {
           this.status = ProductStatus.SOLD_OUT;
        }
    }
}
