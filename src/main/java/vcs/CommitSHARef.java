package vcs;

public class CommitSHARef implements CommitRef{
    private String commitSHA;

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
