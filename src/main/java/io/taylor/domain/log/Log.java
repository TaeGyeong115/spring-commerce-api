package io.taylor.domain.log;

import io.taylor.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EqualsAndHashCode(callSuper = true)
@Table(name = "logs", indexes = {@Index(name = "logs_idx_id", columnList = "id", unique = true)})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Log extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long targetId;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Builder
    Log(Long id, Long memberId, Long targetId, TargetType targetType, ActionType actionType) {
        this.memberId = memberId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.actionType = actionType;
    }

}
