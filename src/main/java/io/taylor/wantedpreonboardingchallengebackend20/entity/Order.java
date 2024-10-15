package io.taylor.wantedpreonboardingchallengebackend20.entity;

import io.taylor.wantedpreonboardingchallengebackend20.dto.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@Getter
@EqualsAndHashCode(callSuper = true)
@Table(name = "orders", indexes = {
        @Index(name = "orders_idx_id", columnList = "id", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long productId;

    private long customerId;

    private BigDecimal price;

    private int quantity;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(long productId, long customerId, BigDecimal price, int quantity) {
        this.productId = productId;
        this.customerId = customerId;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = price.multiply(BigDecimal.valueOf(quantity));
        this.status = OrderStatus.ORDER_IN_PROGRESS;
    }
}

