package vcs;

/**
 * Signals an attempt to delete branch to which HEAD points.
 */
public class DeleteActiveBranchException extends VCSException {
    DeleteActiveBranchException(String s) {
        super(s);
    }
}
