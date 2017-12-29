package vcs;

/**
 * Signals an attempt to reference branch that does not exist.
 */
public class BranchNotFoundException extends VCSException {
    BranchNotFoundException(String branchName) {
        super("Branch with name " + branchName + " doesn't exist!");
    }
}
