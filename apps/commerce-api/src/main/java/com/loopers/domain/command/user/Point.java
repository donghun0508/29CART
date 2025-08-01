package com.loopers.domain.command.user;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.command.shared.Money;
import com.loopers.support.error.user.PointException;
import com.loopers.support.error.user.UserErrorType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
    name = "point"
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseEntity {

    @OneToOne
    @JoinColumn(
        name = "user_id",
        foreignKey = @ForeignKey(name = "FK_POINT_USER_ID"),
        nullable = false,
        updatable = false
    )
    private User user;

    @AttributeOverride(name = "value", column = @Column(name = "balance"))
    private Money balance;

    private Point(User user, Money balance) {
        this.user = user;
        this.balance = balance;
    }

    static Point initial(User user) {
        return new Point(user, Money.ZERO);
    }

    void deduct(Money amount) {
        this.balance = this.balance.subtract(amount);
    }

    void credit(Money amount) {
        if (!amount.isPositive()) {
            throw new PointException(UserErrorType.INVALID_CHARGE_AMOUNT);
        }
        this.balance = this.balance.add(amount);
    }
}
