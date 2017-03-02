package ru.spbau.shevchenko;

import javax.xml.ws.Holder;
import java.util.function.Supplier;

/**
 * Implementation of {@link Lazy} interface suitable when you need to call {@link #get()} in multiple threads.
 * @param <T> type of a result of computation
 */
@SuppressWarnings("WeakerAccess")
public class MultiThreadedLazy<T> implements Lazy<T> {
    private Supplier<T> supplier = null;
    private volatile Holder<T> result = null;

    /**
     * Creates holder for a lazy computation provided by a {@link Supplier} object.
     * @param supplier computation provider
     */
    public MultiThreadedLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Returns result of a computation.
     * It's guaranteed that computation will be performed only once.
     * @return result of a computations
     */
    @Override
    public T get() {
        if (result == null) {
            synchronized (this){
                if (result == null) {
                    result = new Holder<>(supplier.get());
                    supplier = null;
                }
            }
        }
        return result.value;
    }
}
