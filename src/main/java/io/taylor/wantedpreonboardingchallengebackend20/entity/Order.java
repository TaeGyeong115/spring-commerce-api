package io.taylor.wantedpreonboardingchallengebackend20.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EqualsAndHashCode(callSuper = true)
@Table(name = "orders", indexes = {
        @Index(name = "orders_idx_id", columnList = "id", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseEntity {
    @Column
    private long productId;
    @Column
    private long customerId;
    @Column
    private long price;
    @Column
    private long quantity;

    public Order(long productId, long customerId, long price, long quantity) {
        this.productId = productId;
        this.customerId = customerId;
        this.price = price;
        this.quantity = quantity;
    }
}

