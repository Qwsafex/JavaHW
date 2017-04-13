package vcs;

/**
 * Signals an attempt to create branch with name that is already taken.
 */
class BranchAlreadyExistsException extends VCSException {
    BranchAlreadyExistsException(String s) {
        super(s);
    }
}
