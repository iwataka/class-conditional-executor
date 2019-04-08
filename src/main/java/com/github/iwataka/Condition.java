package com.github.iwataka;

import java.util.function.Function;
import java.util.function.Predicate;

public class Condition<T, T2, R> {

    private final ConditionalExecutor<T, R> executor;

    private final Predicate<T> cond;

    Condition(ConditionalExecutor<T, R> executor, Predicate<T> cond) {
        this.executor = executor;
        this.cond = cond;
    }

    public ConditionalExecutor<T, R> then(Function<T2, R> func) {
        Function<T, R> f = (T t) -> func.apply((T2) t);
        executor.add(new ConditionalFunction(cond, f));
        return executor;
    }

}
