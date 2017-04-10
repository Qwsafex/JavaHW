package vcs;

import java.util.List;

public class Commit {
    private List<ContentlessBlob> files;
    private CommitSHARef prevCommit;
    private long time;
    private String message;

    public Commit(String message, List<ContentlessBlob> files, CommitSHARef prevCommit) {
        this.message = message;
        this.files = files;
        this.time = System.currentTimeMillis();
        this.prevCommit = prevCommit;
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    public CommitSHARef getPrevCommit() {
        return prevCommit;
    }

    public CommitRef getRef() {
        return new CommitSHARef(getSHA());

    }

    private String getSHA() { throw new UnsupportedOperationException();
    }

    public static Commit get(String revision) {
        throw new UnsupportedOperationException();
    }
}
