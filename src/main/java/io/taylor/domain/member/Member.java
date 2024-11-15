package io.taylor.domain.member;

import io.taylor.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EqualsAndHashCode(callSuper = true)
@Table(name = "members", indexes = {
        @Index(name = "members_idx_id", columnList = "id", unique = true)},
        uniqueConstraints = {@UniqueConstraint(columnNames = "email")}
)

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)

public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nickName;
    private String email;
    private String password;

    @Builder
    Member(String name, String nickName, String email, String password) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }
}
