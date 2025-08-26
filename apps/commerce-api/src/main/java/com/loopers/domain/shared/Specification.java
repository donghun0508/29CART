package com.loopers.domain.shared;

public interface Specification<T> {

    boolean isSatisfiedBy(T expected, T candidate);

    default Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }

    default Specification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }

    default Specification<T> not() {
        return new NotSpecification<>(this);
    }

    class AndSpecification<T> implements Specification<T> {

        private final Specification<T> left;
        private final Specification<T> right;

        public AndSpecification(Specification<T> left, Specification<T> right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean isSatisfiedBy(T expected, T candidate) {
            return left.isSatisfiedBy(expected, candidate) && right.isSatisfiedBy(expected, candidate);
        }
    }

    class OrSpecification<T> implements Specification<T> {

        private final Specification<T> left;
        private final Specification<T> right;

        public OrSpecification(Specification<T> left, Specification<T> right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean isSatisfiedBy(T expected, T candidate) {
            return left.isSatisfiedBy(expected, candidate) || right.isSatisfiedBy(expected, candidate);
        }
    }

    class NotSpecification<T> implements Specification<T> {

        private final Specification<T> spec;

        public NotSpecification(Specification<T> spec) {
            this.spec = spec;
        }

        @Override
        public boolean isSatisfiedBy(T expected, T candidate) {
            return !spec.isSatisfiedBy(expected, candidate);
        }
    }
}
