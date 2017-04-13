package vcs;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class CommitSHARef implements CommitRef, SHARef{
    private static final Path COMMIT_DIR = Paths.get("commits");
    private String commitSHA;

    CommitSHARef(String commitSHA) {
        this.commitSHA = commitSHA;
    }

    @Override
    public CommitSHARef getCommitSHA() {
        return this;
    }

    @Override
    public Commit getCommit() throws IOException, ClassNotFoundException {
        return (Commit) VCSFiles.readObject(COMMIT_DIR.resolve(commitSHA));
    }

    /**
     * Adds commit after the one reference points to.
     * @param commit commit to be added after the one reference points to
     * @return reference to added commit
     */
    @Override
    public CommitRef addCommitAfter(Commit commit) {
        return commit.getSHARef();
    }


    @Override
    public String toString() {
        return commitSHA;
    }

    @Override
    public GitObject getObject() throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException();
    }
}
