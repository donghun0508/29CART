package com.loopers.domain.user;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter(AccessLevel.PACKAGE)
@Entity
@Table(name = "point")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        foreignKey = @ForeignKey(name = "FK_POINT_USER_ID"),
        nullable = false,
        updatable = false
    )
    private User user;

    @AttributeOverride(
        name = "value",
        column = @Column(
            name = "balance",
            nullable = false
        )
    )
    private Money balance;

    static Point initialize(User user) {
        requireNonNull(user, "포인트 생성 시 회원은 null일 수 없습니다.");

        Point point = new Point();
        point.user = user;
        point.balance = Money.ZERO;

        return point;
    }

    void increaseBalance(Money amount) {
        if (amount.isNegativeOrZero()) {
            throw new IllegalArgumentException("충전 금액은 양수여야 합니다.");
        }
        this.balance = this.balance.add(amount);
    }

    void decreaseBalance(Money amount) {
        if (amount.isNegativeOrZero()) {
            throw new IllegalArgumentException("사용 금액은 양수여야 합니다.");
        }
        this.balance = this.balance.subtract(amount);
    }
}
