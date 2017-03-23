package vcs;

public class BranchAlreadyExistsException extends Throwable {
    public BranchAlreadyExistsException(String s) {
        super(s);
    }
}
