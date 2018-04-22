package ru.spbau.shevchenko;

/**
 * An object that holds a result of lazy computation.
 *
 * It should be the same object on all calls of {@link Lazy#get() ru.spbau.shevchenko.Lazy::get()} method.
 *
 * @param <T> type of a result of computation
 */
@SuppressWarnings("WeakerAccess")
public interface Lazy<T> {
    /**
     * Method that performs computation.
     * @return result of computation
     */
    T get();
}
