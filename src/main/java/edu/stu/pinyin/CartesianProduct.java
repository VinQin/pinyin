package edu.stu.pinyin;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

public class CartesianProduct {

    public List<?> product(List<?>... a) {
        if (a.length > 0) {
            List<?> product = a[0];
            for (int i = 1; i < a.length; i++) {
                product = productOfTwo(product, a[i]);
            }
            return product;
        }

        return emptyList();
    }

    private <A, B> List<?> productOfTwo(List<A> a, List<B> b) {
        return of(a.stream()
                .map(e1 -> of(b.stream().map(e2 -> asList(e1, e2)).collect(toList())).orElse(emptyList()))
                .flatMap(List::stream)
                .collect(toList())).orElse(emptyList());
    }

    public String toString(List<?> lists, final boolean tradition) {
        if (tradition) {
            return lists.toString();
        }
        return toString(lists);
    }

    public String toString(List<?> lists) {
        if (0 == lists.size()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(convertListToString(lists, true));
        sb.append("]");
        return sb.toString();
    }

    private boolean sign;

    private StringBuilder convertListToString(List<?> lists, final boolean flag) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = lists.size(); i < len; i++) {
            if (flag) {
                if (0 == i) {
                    sb.append("[");
                } else {
                    sb.append(" ,[");
                }
                sign = true;
            }
            Object list = lists.get(i);
            if (list instanceof List) {
                sb.append(convertListToString((List<?>) list, false));
            } else {
                if (!sign) {
                    sb.append(", ");
                }
                sb.append(list.toString());
                sign = false;
            }

            if (flag) {
                sb.append("]");
            }
        }
        return sb;
    }


}