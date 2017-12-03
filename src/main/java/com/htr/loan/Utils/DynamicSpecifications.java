package com.htr.loan.Utils;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DynamicSpecifications {
    private static final ConversionService conversionService = new DefaultConversionService();

    public static <T> Specification<T> bySearchFilter(
            final Collection<SearchFilter> filters, final Class<T> clazz) {
        return (root, query, builder) -> {
            if (!ObjectUtils.isEmpty(filters)) {
                List<Predicate> predicates = new ArrayList<>();
                for (SearchFilter filter : filters) {

                    String[] names = filter.fieldName.split("\\.");
                    Path expression = root.get(names[0]);
                    if (expression.getJavaType().equals(List.class)) {
                        Join join = root.join(names[0], JoinType.LEFT);
                        expression = join.get(names[1]);
                    } else {
                        for (int i = 1; i < names.length; i++) {
                            expression = expression.get(names[i]);
                        }
                    }

                    // convert value from string to target type
                    Class attributeClass = expression.getJavaType();
                    if (!attributeClass.equals(String.class)
                            && filter.value instanceof String
                            && conversionService.canConvert(String.class,
                            attributeClass)) {
                        filter.value = conversionService.convert(
                                filter.value, attributeClass);
                    }

                    // logic operator
                    switch (filter.operator) {
                        case EQ:
                            predicates.add(builder.equal(expression, filter.value));
                            break;
                        case NE:
                            predicates.add(builder.notEqual(expression, filter.value));
                            break;
                        case IN:
                            predicates.add(expression.in(((List) filter.value).toArray()));
                            break;
                        case ISNULL:
                            predicates.add(builder.isNull(expression));
                            break;
                        case LIKE:
                            predicates.add(builder.like(expression, "%" + filter.value + "%"));
                            break;
                        case LIKEIGNORE:
                            predicates.add(builder.like(builder.lower(expression), "%" + filter.value.toString().toLowerCase() + "%"));
                            break;
                        case GT:
                            predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
                            break;
                        case LT:
                            predicates.add(builder.lessThan(expression, (Comparable) filter.value));
                            break;
                        case GTE:
                            predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
                            break;
                        case LTE:
                            predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
                            break;
                    }
                }

                // 将所有条件用 and 联合起来
                if (predicates.size() > 0) {
                    query.distinct(true);
                    return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            }
            return builder.conjunction();
        };
    }
}
