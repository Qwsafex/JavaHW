package ru.spbau.shevchenko;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;


public class LazyFactoryTest {
    private static final int THREAD_NUMBER = 10;

    private class NaturalNumbersSupplier implements Supplier<Integer> {
        private int current = 1;
        @Override
        public Integer get() {
            return current++;
        }
    };
    private static final Supplier<Object> oneTimeNullSupplier = new Supplier<Object>() {
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


    @Test
    public void testSingleThreadedIfSame() throws Exception {
        testIfSame(LazyFactory.createSingleThreadedLazy(new NaturalNumbersSupplier()), 1);
    }
    @Test
    public void testMultiThreadedIfSame() throws Exception {
        testIfSame(LazyFactory.createMultiThreadedLazy(new NaturalNumbersSupplier()), 1);
    }
    @Test
    public void testLockFreeIfSame() throws Exception {
        testIfSame(LazyFactory.createLockFreeLazy(new NaturalNumbersSupplier()), 1);
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



    @Test
    public void testMultiThreadedRaces() throws InterruptedException {
        testInMultipleThreads(LazyFactory.createMultiThreadedLazy(new NaturalNumbersSupplier()));
    }
    @Test
    public void testLockFreeRaces() throws InterruptedException {
        testInMultipleThreads(LazyFactory.createLockFreeLazy(new NaturalNumbersSupplier()));
    }

    private void testInMultipleThreads(Lazy<Integer> oneLazy) throws InterruptedException {
        final List<Thread> threads = new ArrayList<>();
        final Runnable runnable = () -> testIfSame(oneLazy, 1);
        for (int i = 0; i < THREAD_NUMBER; i++) {
            final Thread thread = new Thread(runnable);
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private void testOnNull(Lazy<Object> nullLazy) {
        assertEquals(null, nullLazy.get());
        assertEquals(null, nullLazy.get());
    }
    private void testIfSame(Lazy<Integer> lazyOne, Integer expected) {
        assertEquals(expected, lazyOne.get());
        assertEquals(expected, lazyOne.get());

    }

}