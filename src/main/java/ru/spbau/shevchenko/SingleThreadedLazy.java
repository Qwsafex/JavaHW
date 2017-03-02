package ru.spbau.shevchenko;

import javax.xml.ws.Holder;
import java.util.function.Supplier;

/**
 * Implementation of {@link Lazy} interface suitable when you only need to call {@link #get()} in single thread.
 * @param <T> type of a result of computation
 */

@SuppressWarnings("WeakerAccess")
public class SingleThreadedLazy<T> implements Lazy<T> {
    private Supplier<T> supplier = null;
    private Holder<T> result = null;

    /**
     * Creates holder for a lazy computation provided by a {@link Supplier} object.
     * @param supplier computation provider
     */
    public SingleThreadedLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Returns result of a computation.
     * It's guaranteed that computation will be performed only once.
     * @return result of a computation
     */
    @Override
    public T get() {
        if (result == null){
            result = new Holder<>(supplier.get());
            supplier = null;
        }
        return result.value;
    }
}
