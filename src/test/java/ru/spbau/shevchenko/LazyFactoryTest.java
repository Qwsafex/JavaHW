package ru.spbau.shevchenko;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;


public class LazyFactoryTest {
    private Supplier<Integer> naturalNumbersSupplier = new Supplier<Integer>() {
        private int current = 1;
        @Override
        public Integer get() {
            return current++;
        }
    };


    @Test
    public void testSingleThreadedIfSame() throws Exception {
        testIfSame(LazyFactory.createSingleThreadedLazy(naturalNumbersSupplier), 1);
    }
    @Test
    public void testMultiThreadedIfSame() throws Exception {
        testIfSame(LazyFactory.createMultiThreadedLazy(naturalNumbersSupplier), 1);
    }
    @Test
    public void testLockFreeIfSame() throws Exception {
        testIfSame(LazyFactory.createLockFreeLazy(naturalNumbersSupplier), 1);
    }
    @Test
    public void testSingleThreadedOnNull() throws Exception {
        testOnNull(LazyFactory.createSingleThreadedLazy(oneTimeNullSupplier));
    }
    @Test
    public void testMultiThreadedOnNull() throws Exception {
        testOnNull(LazyFactory.createMultiThreadedLazy(oneTimeNullSupplier));
    }
    @Test
    public void testLockFreeOnNull() throws Exception {
        testOnNull(LazyFactory.createLockFreeLazy(oneTimeNullSupplier));
    }

    private void testOnNull(Lazy<Object> nullLazy) {
        assertEquals(null, nullLazy.get());
        assertEquals(null, nullLazy.get());
    }
    private void testIfSame(Lazy<Integer> lazyOne, Integer expected) {
        assertEquals(expected, lazyOne.get());
        assertEquals(expected, lazyOne.get());

    }

    private Supplier<Object> oneTimeNullSupplier = new Supplier<Object>() {
        private int timesCalled = 0;

        @Override
        public Object get(){
            if (timesCalled == 0) {
                return null;
            }
            else {
                throw new RuntimeException();
            }
        }
    };
}