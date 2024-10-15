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

    private long productId;

    private long customerId;

    private long price;

    private long quantity;

    private long totalPrice;

    private int status;

    public Order(long productId, long customerId, long price, long quantity) {
        this.productId = productId;
        this.customerId = customerId;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = price * quantity;
        this.status = 0;
    }
}

