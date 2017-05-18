package xunit;

public class IllegalTestClassException extends Exception {
    public IllegalTestClassException() {
    }

    public IllegalTestClassException(String s) {
        super(s);
    }

    public IllegalTestClassException(String s, Exception e) {
        super(s, e);
    }
}
