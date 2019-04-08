package com.github.iwataka;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConditionalExecutor<T, R> {

    private final List<ConditionalFunction<T, R>> funcList;

    public ConditionalExecutor() {
        funcList = new ArrayList<>();
    }

    public <T2> Condition<T, T2, R> whenInstanceOf(Class<T2> clazz) {
        Predicate<T> cond = (T t) -> clazz.isInstance(t);
        return new Condition<>(this, cond);
    }

    public R apply(T obj) throws NotMatchAnyConditionException {
        return funcList.stream()
                .filter(f -> f.canApply(obj))
                .findFirst()
                .map(f -> f.apply(obj))
                .orElseThrow(() -> new NotMatchAnyConditionException());
    }

    void add(ConditionalFunction<T, R> func) {
        funcList.add(func);
    }

}

class ConditionalFunction<T, R> {

    private final Predicate<T> cond;

    private final Function<T, R> func;

    ConditionalFunction(Predicate<T> cond, Function<T, R> func) {
        this.cond = cond;
        this.func = func;
    }

    boolean canApply(T t) {
        return cond.test(t);
    }

    R apply(T t) {
        return func.apply(t);
    }
}

