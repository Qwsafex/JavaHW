package vcs;

public class BranchNotFoundException extends VCSException {
    public BranchNotFoundException(String branchName) {
        super("Branch with name " + branchName + " doesn't exist!");
    }
}
