package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for methods that are to be tested.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XTest {
    /**
     * Reason to ignore this test method. Empty string if it should not be ignored.
     * @return reason why this test method should be ignored or empty string if it shouldn't
     */
    String ignore() default "";

    /**
     * Expected exception or {@link NoException} if no exception is expected.
     * @return class of exception that is expected to be thrown
     */
    Class<? extends Throwable> expected() default NoException.class;

    /**
     * Stub class for {@link XTest#expected()} methods that don't throw any exception.
     */
    final class NoException extends Throwable {
        private NoException() {
        }
    }
}