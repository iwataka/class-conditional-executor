package com.github.iwataka;

import java.util.function.Function;
import java.util.function.Predicate;

public class Condition<T, T2, R> {

    private final ConditionalFunctionExecutor<T, R> executor;

    private final Predicate<T> cond;

    Condition(ConditionalFunctionExecutor<T, R> executor, Predicate<T> cond) {
        this.executor = executor;
        this.cond = cond;
    }

    public ConditionalFunctionExecutor<T, R> then(Function<T2, R> func) {
        final Function<T, R> f = (T t) -> func.apply((T2) t);
        executor.addFunction(new ConditionalFunction(cond, f));
        return executor;
    }

    public ConditionalFunctionExecutor<T, R> then(R val) {
        final Function<T2, R> f = (T2 t) -> val;
        return then(f);
    }

}
