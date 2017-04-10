package vcs;

public class Branch implements CommitRef {
    private CommitSHARef headCommit;
    private String name;


    public Branch(CommitSHARef headCommit) {
        this.headCommit = headCommit;
    }


    @Override
    public CommitSHARef getCommitSHA() {
        return headCommit.getCommitSHA();
    }

    @Override
    public Commit getCommit() {
        return headCommit.getCommit();
    }

    @Override
    public CommitRef addCommitAfter(Commit commit) {
        throw new UnsupportedOperationException();
    }

}
