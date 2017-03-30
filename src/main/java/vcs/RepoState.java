package vcs;

import java.util.List;

public class RepoState {
    private List<ContentlessBlob> files;

    public void add(ContentlessBlob blob) {
        throw new UnsupportedOperationException();
    }
    public void remove(ContentlessBlob blob) {
        throw new UnsupportedOperationException();
    }

    public boolean empty() {
        return files.isEmpty();
    }

    public void updateWith(RepoState delta) {
        throw new UnsupportedOperationException();
    }

    public List<ContentlessBlob> getFiles() {
        return files;
    }

    public static RepoState getFromCommit(Commit commit) {
        throw new UnsupportedOperationException();
    }
}
