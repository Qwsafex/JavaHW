package vcs;

public class BranchNotFoundException extends VCSException {
    @SuppressWarnings("WeakerAccess")
    public BranchNotFoundException(String branchName) {
        super("Branch with name " + branchName + " doesn't exist!");
    }
}
