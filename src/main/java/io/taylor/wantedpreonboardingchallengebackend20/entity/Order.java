package io.taylor.wantedpreonboardingchallengebackend20.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="orders",  indexes={
        @Index(name="orders_idx_id", columnList="id", unique=true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseEntity {
    @Column
    private long productId;
    @Column
    private long customerId;
    @Column
    private long providerId;
    @Column
    private long price;

    public Order(long productId, long customerId, long providerId, long price) {
        this.productId = productId;
        this.customerId = customerId;
        this.providerId = providerId;
        this.price = price;
    }
}
