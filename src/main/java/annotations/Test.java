package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which allows to mark methods as test methods for XUnit .
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    String ignore() default "";

    Class<? extends Throwable> expected() default NoException.class;

    final class NoException extends Throwable {

        private NoException() {
        }
    }
}