package vcs;

public class Branch implements CommitRef {
    private CommitSHARef headCommit;
    private String name;


    public Branch(CommitSHARef headCommit) {
        this.headCommit = headCommit;
    }


    @Override
    public CommitSHARef getCommitSHA() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Commit getCommit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CommitRef addCommitAfter(Commit commit) {
        throw new UnsupportedOperationException();
    }

}
