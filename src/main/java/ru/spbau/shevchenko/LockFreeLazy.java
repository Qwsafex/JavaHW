package ru.spbau.shevchenko;

import javax.xml.ws.Holder;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

/**
 * Lock-free implementation of {@link Lazy} interface suitable when you need to call {@link #get()} in multiple threads.
 * @param <T> type of a result of computation
 */
@SuppressWarnings("WeakerAccess")
public class LockFreeLazy<T> implements Lazy<T> {
    private Supplier<T> supplier = null;
    private volatile Holder<T> result = null;

    @SuppressWarnings("AtomicFieldUpdaterIssues")
    private static final AtomicReferenceFieldUpdater<LockFreeLazy, Holder> resultUpdater =
            AtomicReferenceFieldUpdater.newUpdater(LockFreeLazy.class, Holder.class, "result");

    /**
     * Creates holder for a lazy computation provided by a {@link Supplier} object.
     * @param supplier computation provider
     */
    public LockFreeLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }


    /**
     * Returns result of a computation.
     * Computation may be performed several times, but this method will always return the same object.
     * @return result of a computations
     */
    @Override
    public T get() {
        if (result == null) {
            final Holder<T> value = new Holder<>(supplier.get());
            resultUpdater.compareAndSet(this, null, value);
        }
        return result.value;
    }
}
