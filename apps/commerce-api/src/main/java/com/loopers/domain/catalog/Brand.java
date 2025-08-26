package com.loopers.domain.catalog;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "brand")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {

    private String name;

}

// command
// 외부 노출
// 애그리게이트, 도메인 서비스, ProductService

// query

// 내부용
// Repository, Entity,
