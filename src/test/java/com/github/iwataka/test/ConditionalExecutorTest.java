package com.github.iwataka.test;

import com.github.iwataka.ConditionalExecutor;
import com.github.iwataka.NotMatchAnyConditionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConditionalExecutorTest {

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

    private static final ConditionalExecutor<Object, Object> keyGetter = new ConditionalExecutor<>()
            .whenInstanceOf(Foo.class)
            .then(Foo::getKey1)
            .whenInstanceOf(Bar.class)
            .then(Bar::getKey2);


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
}
