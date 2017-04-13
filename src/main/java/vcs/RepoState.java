package vcs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

class RepoState {
    private HashSet<ContentlessBlob> files;
    private Path filePath = null;

    private RepoState() {
        files = new HashSet<>();
    }
    RepoState(String stateName) throws IOException, ClassNotFoundException {
        filePath = Paths.get(stateName);
        if (VCSFiles.exists(filePath)) {
            //noinspection unchecked
            files = (HashSet<ContentlessBlob>) VCSFiles.readObject(filePath);
        }
        else {
            VCSFiles.create(filePath);
        }
        files = new HashSet<>();
    }

    void add(ContentlessBlob blob) throws IOException {
        if (files.contains(blob)) {
            return;
        }
        files.add(blob);
        if (filePath != null) {
            VCSFiles.writeObject(filePath, files);
        }
    }
    @SuppressWarnings("unused")
    public void remove(ContentlessBlob blob) throws IOException {
        if (!files.contains(blob)) {
            return; // TODO: throw exception?
        }
        files.remove(blob);
        if (filePath != null) {
            VCSFiles.writeObject(filePath, files);
        }
    }

    boolean empty() {
        return files.isEmpty();
    }

    void updateWith(RepoState delta) {
        files.addAll(delta.getFiles());
    }

    List<ContentlessBlob> getFiles() {
        return files.stream().collect(Collectors.toList());
    }

    static RepoState getFromCommit(Commit commit) throws IOException, ClassNotFoundException {
        RepoState result = new RepoState();
        while (commit != null) {
            List<ContentlessBlob> blobs = commit.getFiles();
            for (ContentlessBlob blob : blobs) {
                result.add(blob);
            }
            commit = commit.getPrevCommit().getCommit();
        }
        return result;
    }
}
