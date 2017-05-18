package xunit;

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
