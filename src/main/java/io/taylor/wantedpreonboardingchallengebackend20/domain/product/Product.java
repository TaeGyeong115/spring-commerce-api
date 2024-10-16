package io.taylor.wantedpreonboardingchallengebackend20.domain.product;

import io.taylor.wantedpreonboardingchallengebackend20.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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

    private static final int INITIAL_SOLD_QUANTITY = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private long providerId;

    private int totalQuantity;

    private int soldQuantity;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Builder
    private Product(Long id, long providerId, String name, int price, int totalQuantity) {
        validatePrice(price);
        validateQuantity(totalQuantity);

        this.id = id;
        this.providerId = providerId;
        this.name = name;
        this.price = BigDecimal.valueOf(price);
        this.totalQuantity = totalQuantity;
        this.soldQuantity = INITIAL_SOLD_QUANTITY;
        this.status = ProductStatus.FOR_SALE;
    }

    public int remainingQuantity() {
        return this.totalQuantity - this.soldQuantity;
    }

    private void increaseSoldQuantity(int quantity) {
        this.soldQuantity += quantity;
    }

    public void processSale(int quantity) {
        increaseSoldQuantity(quantity);

        if (this.totalQuantity == this.soldQuantity) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }

    private void validatePrice(int price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }
}
