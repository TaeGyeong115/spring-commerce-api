package io.taylor.wantedpreonboardingchallengebackend20.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.taylor.wantedpreonboardingchallengebackend20.model.ProductStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@Entity
@Table(name="products")
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private long price;
    @Column
    private int status;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
        this.status = ProductStatus.Available.getValue();
    }
}
