package com.github.iwataka.test;

import com.github.iwataka.ConditionalFunctionExecutor;
import com.github.iwataka.NotMatchAnyConditionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConditionalFunctionExecutorTest {

    private class Foo {

        private final Object key1;

        public Foo(Object key1) {
            this.key1 = key1;
        }

        public Object getKey1() {
            return key1;
        }
    }

    private class Foo2 extends Foo {

        public Foo2(Object key1) {
            super(key1);
        }

    }

    private class Bar {

        private final Object key2;

        public Bar(Object key2) {
            this.key2 = key2;
        }

        public Object getKey2() {
            return key2;
        }
    }

    private static final ConditionalFunctionExecutor<Object, Object> keyGetter = new ConditionalFunctionExecutor<>()
            .whenInstanceOf(Foo.class)
            .then(Foo::getKey1)
            .whenInstanceOf(Bar.class)
            .then(Bar::getKey2);

    private static final ConditionalFunctionExecutor<Object, String> classNameGetter = new ConditionalFunctionExecutor<Object, String>()
            .whenInstanceOf(Foo.class)
            .then(Foo.class.getSimpleName())
            .whenInstanceOf(Bar.class)
            .then(Bar.class.getSimpleName());


    @Test
    public void test_apply() throws NotMatchAnyConditionException {
        Foo foo = new Foo(new Object());
        Bar bar = new Bar(new Object());
        Foo2 foo2 = new Foo2(new Object());

        assertEquals(foo.getKey1(), keyGetter.apply(foo));
        assertEquals(bar.getKey2(), keyGetter.apply(bar));
        assertEquals(foo2.getKey1(), keyGetter.apply(foo2));
        assertNotEquals(foo.getKey1(), keyGetter.apply(foo2));
    }

    @Test
    public void test_apply_throwException() {
        assertThrows(NotMatchAnyConditionException.class, () -> keyGetter.apply(new Object()));
    }

    @Test
    public void test_apply_forConstantExecutor() throws NotMatchAnyConditionException {
        assertEquals(Foo.class.getSimpleName(), classNameGetter.apply(new Foo(new Object())));
        assertEquals(Bar.class.getSimpleName(), classNameGetter.apply(new Bar(new Object())));
    }
}
