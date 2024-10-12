package io.taylor.wantedpreonboardingchallengebackend20.entity;

import io.taylor.wantedpreonboardingchallengebackend20.dto.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EqualsAndHashCode(callSuper = true)
@Table(name = "products", indexes = {
        @Index(name = "products_idx_id", columnList = "id", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product extends BaseEntity {
    @Column
    private String name;
    @Column
    private long providerId;
    @Column
    private long quantity;
    @Column
    private long price;
    @Column
    private int status;

    public Product(long providerId, String name, long price, long quantity) {
        this.providerId = providerId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = ProductStatus.Available.getNumber();
    }
}
