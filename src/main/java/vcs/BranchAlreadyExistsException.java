package vcs;

/**
 * Signals an attempt to create branch with name that is already taken.
 */
public class BranchAlreadyExistsException extends VCSException {
    BranchAlreadyExistsException(String s) {
        super(s);
    }
}
