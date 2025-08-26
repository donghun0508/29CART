package com.loopers.domain.user;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Getter
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_USER_ACCOUNT_ID", columnNames = {"account_id"}),
        @UniqueConstraint(name = "UK_USER_EMAIL", columnNames = {"email"})
    }
)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @NaturalId
    @AttributeOverride(
        name = "value",
        column = @Column(
            name = "account_id",
            nullable = false,
            unique = true
        )
    )
    private AccountId accountId;

    @AttributeOverride(
        name = "address",
        column = @Column(
            name = "email",
            nullable = false,
            unique = true
        )
    )
    private Email email;

    @AttributeOverride(
        name = "day",
        column = @Column(
            name = "birth",
            nullable = false
        )
    )
    private Birth birth;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Point point;

    @Column(
        name = "gender",
        nullable = false,
        updatable = false
    )
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public static User of(String accountId, String email, String birth, Gender gender) {
        User user = new User();

        user.accountId = AccountId.of(accountId);
        user.email = Email.of(email);
        user.birth = Birth.of(birth);
        user.gender = gender;
        user.point = Point.initialize(user);

        return user;
    }

    public void chargePoint(Money amount) {
        requireNonNull(amount, "충전할 포인트는 null일 수 없습니다.");
        this.point.increaseBalance(amount);
    }

    public void payWithPoints(Money amount) {
        requireNonNull(amount, "사용할 포인트는 null일 수 없습니다.");
        this.point.decreaseBalance(amount);
    }

    public Money getTotalPoint() {
        return this.point.getBalance();
    }

}
