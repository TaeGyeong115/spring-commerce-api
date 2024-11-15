package io.taylor.domain.product;

import io.taylor.domain.BaseEntity;
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

    @Version
    private int soldQuantity;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Builder
    Product(Long id, long providerId, String name, BigDecimal price, int totalQuantity) {
        validatePrice(price);
        validateQuantity(totalQuantity);

        this.id = id;
        this.providerId = providerId;
        this.name = name;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.soldQuantity = INITIAL_SOLD_QUANTITY;
        this.status = ProductStatus.FOR_SALE;
    }

    public int remainingQuantity() {
        return this.totalQuantity - this.soldQuantity;
    }

    public void processSale(int quantity) {
        increaseSoldQuantity(quantity);

        if (this.totalQuantity == this.soldQuantity) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }

    private void increaseSoldQuantity(int quantity) {
        if (remainingQuantity() < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        this.soldQuantity += quantity;
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(new BigDecimal("100")) < 0) {
            throw new IllegalArgumentException("가격은 100원 이상입니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
    }
}
