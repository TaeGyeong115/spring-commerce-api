package io.taylor.wantedpreonboardingchallengebackend20.domain.member;

import io.taylor.wantedpreonboardingchallengebackend20.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickName;

    private String email;

    private String password;

    @Builder
    public Member(Long id, String name, String nickName, String email, String password) {
        validateEmail(email);

        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    private static void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("유효하지 않은 이메일 주소입니다.");
        }
    }
}
