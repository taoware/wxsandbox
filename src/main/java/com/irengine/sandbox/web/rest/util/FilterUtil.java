package com.irengine.sandbox.web.rest.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FilterUtil {

    public static <T> Specification<T> generateSpecifications(final Collection<Filter> filters, final Class<T> clazz) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                if (!filters.isEmpty()) {
                    List<Predicate> predicates = new ArrayList<>();
                    for (Filter filter : filters) {
                        // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
                        String[] names = StringUtils.split(filter.getField(), ".");
                        Path expression = root.get(names[0]);
                        for (int i = 1; i < names.length; i++) {
                            expression = expression.get(names[i]);
                        }

                        // logic operator
                        switch (filter.getOperator()) {
                            case EQ:
                                predicates.add(builder.equal(expression, filter.getValue()));
                                break;
                            case LIKE:
                                predicates.add(builder.like(expression, "%" + filter.getValue() + "%"));
                                break;
                            case GT:
                                predicates.add(builder.greaterThan(expression, (Comparable) filter.getValue()));
                                break;
                            case LT:
                                predicates.add(builder.lessThan(expression, (Comparable) filter.getValue()));
                                break;
                            case GTE:
                                predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.getValue()));
                                break;
                            case LTE:
                                predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.getValue()));
                                break;
                        }
                    }

                    if (predicates.size() > 0) {
                        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                    }
                }

                return builder.conjunction();
            }
        };
    }
}
