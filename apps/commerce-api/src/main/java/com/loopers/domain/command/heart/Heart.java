package com.loopers.domain.command.heart;

import static com.loopers.utils.ValidationUtils.requireNonNull;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
    name = "heart"
    , uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_HEART_USER_TARGET_TYPE",
            columnNames = {"user_id", "target_id", "target_type"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart extends BaseEntity {

    @Column(
        name = "user_id",
        nullable = false,
        updatable = false
    )
    private Long userId;

    @AttributeOverrides({
        @AttributeOverride(name = "targetId", column = @Column(name = "target_id", nullable = false, updatable = false)),
        @AttributeOverride(name = "targetType", column = @Column(name = "target_type", nullable = false, updatable = false))
    })
    private Target target;

    public static Heart create(HeartCommand.Create command) {
        requireNonNull(command, "좋아요 명령이 null일 수 없습니다.");

        Heart heart = new Heart();
        heart.userId = command.userId();
        heart.target = command.target();

        return heart;
    }
}
