package com.github.maximovj.rhhub_app.repository.specification;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification<T> {

    protected Specification<T> spec = Specification.where(null);

    protected Specification<T> equalsSpec(String field, Object value) {
        return (root, query, cb) ->
                value == null ? null : cb.equal(root.get(field), value);
    }

    protected Specification<T> likeIgnoreCase(String field, String value) {
        return (root, query, cb) ->
                (value == null || value.isBlank())
                        ? null
                        : cb.like(
                            cb.lower(root.get(field)),
                            "%" + value.toLowerCase() + "%"
                        );
    }

    protected Specification<T> dateEquals(String field, LocalDateTime value) {
        return (root, query, cb) ->
                value == null ? null : cb.equal(root.get(field), value);
    }

    protected Specification<T> dateBetween(String field, LocalDateTime desde, LocalDateTime hasta) {
        return (root, query, cb) -> {
            if (desde != null && hasta != null) {
                return cb.between(root.get(field), desde, hasta);
            } else if (desde != null) {
                return cb.greaterThanOrEqualTo(root.get(field), desde);
            } else if (hasta != null) {
                return cb.lessThanOrEqualTo(root.get(field), hasta);
            } else {
                return null;
            }
        };
    }

    public Specification<T> build(){ 
        return spec;
    }
    
}
