package com.loopers.domain.heart;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter(AccessLevel.PACKAGE)
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

    public static Heart from(Long userId, Target target) {

        Heart heart = new Heart();
        heart.userId = userId;
        heart.target = target;

        return heart;
    }
}
