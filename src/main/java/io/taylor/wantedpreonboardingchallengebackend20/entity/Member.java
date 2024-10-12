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
@Table(name = "members", indexes = {
        @Index(name = "members_idx_id", columnList = "id", unique = true)
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {
    @Column
    private String name;
    @Column
    private String nickName;
    @Column(unique = true)
    private String email;
    @Column
    private String password;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String name, String nickName, String email, String password) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }
}
