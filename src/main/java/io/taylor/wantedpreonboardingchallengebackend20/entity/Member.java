package io.taylor.wantedpreonboardingchallengebackend20.entity;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.MemberJoinRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "members", indexes = {
        @Index(name = "members_idx_id", columnList = "id", unique = true)
})
@Entity
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

    public Member(MemberJoinRequest request) {
        this.name = request.getName();
        this.nickName = request.getNickName();
        this.email = request.getEmail();
        this.password = request.getPassword();
    }
}
