package vcs;

public interface CommitRef {
    CommitSHARef getCommitSHA();
    Commit getCommit();
    CommitRef addCommitAfter(Commit commit);
}
