package xunit;


/**
 * Exception that is thrown if test class or its methods are malformed.
 */
@SuppressWarnings("WeakerAccess")
public class IllegalTestClassException extends Exception {
    IllegalTestClassException() {
    }

    IllegalTestClassException(String s) {
        super(s);
    }

    IllegalTestClassException(String s, Exception e) {
        super(s, e);
    }
}
