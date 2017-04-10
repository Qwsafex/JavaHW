package vcs;

public class CommitSHARef implements CommitRef{
    private String commitSHA;

    public CommitSHARef(String commitSHA) {
        this.commitSHA = commitSHA;
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
