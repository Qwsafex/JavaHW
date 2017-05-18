package xunit;

public class IllegalTestClassException extends Throwable {
    public IllegalTestClassException() {
    }

    public IllegalTestClassException(String s) {
        super(s);
    }

    public IllegalTestClassException(String s, Exception e) {
        super(s, e);
    }
}
