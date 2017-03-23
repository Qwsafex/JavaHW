package vcs;

public class NothingToCommitException extends Throwable {
    @SuppressWarnings("WeakerAccess")
    public NothingToCommitException(String s) {
        super(s);
    }
}
