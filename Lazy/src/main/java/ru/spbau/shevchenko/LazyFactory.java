package ru.spbau.shevchenko;

import java.util.function.Supplier;

/**
 * Class ru.spbau.shevchenko.LazyFactory provides factory methods for creating instances of different {@link Lazy} implementations.
 */
public class LazyFactory {
    private LazyFactory(){}

    /**
     * Returns {@link Lazy} object suitable for use in a single thread.
     * It's guaranteed, that computation will be done only once.
     * @param supplier computation to perform
     * @param <T> type of computation result
     * @return result of computation
     */
    public static <T> Lazy<T> createSingleThreadedLazy(Supplier<T> supplier){
        return new SingleThreadedLazy<>(supplier);
    }

    /**
     * Returns {@link Lazy} object suitable for use in multiple threads.
     * It's guaranteed, that computation will be done only once.
     * @param supplier computation to perform.
     * @param <T> type of computation result
     * @return result of computation
     */
    public static <T> Lazy<T> createMultiThreadedLazy(Supplier<T> supplier){
        return new MultiThreadedLazy<>(supplier);
    }

    /**
     * Returns {@link Lazy} object suitable for use in multiple threads.
     * No locks on object will be done.
     * Computation may be performed several times but resulting object will always be the same
     * @param supplier computation to perform.
     * @param <T> type of computation result
     * @return result of computation
     */
    public static <T> Lazy<T> createLockFreeLazy(Supplier<T> supplier){
        return new LockFreeLazy<>(supplier);
    }
}
