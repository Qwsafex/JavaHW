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
            files = new HashSet<>();
            VCSFiles.create(filePath);
            VCSFiles.writeObject(filePath, files);
        }
    }

    void add(ContentlessBlob blob) throws IOException {
        if (files.contains(blob)) {
            return;
        }
        files.add(blob);
        flush();
    }
    @SuppressWarnings("unused")
    public void remove(ContentlessBlob blob) throws IOException {
        if (!files.contains(blob)) {
            return; // TODO: throw exception?
        }
        files.remove(blob);
        flush();
    }

    boolean empty() {
        return files.isEmpty();
    }

    void updateWith(RepoState delta) throws IOException {
        files.addAll(delta.getFiles());
        flush();
    }

    List<ContentlessBlob> getFiles() {
        return files.stream().collect(Collectors.toList());
    }

    static RepoState getFromCommit(Commit commit) throws IOException, ClassNotFoundException {
        System.out.println("getFromCommit-1");
        RepoState result = new RepoState();
        while (commit != null) {
            System.out.println("getFromCommit-2");
            List<ContentlessBlob> blobs = commit.getFiles();
            System.out.println("getFromCommit-3");
            for (ContentlessBlob blob : blobs) {
                result.add(blob);
            }
            System.out.println("getFromCommit-4");
            if (commit.getPrevCommit() == null) {
                break;
            }
            commit = commit.getPrevCommit().getCommit();
        }
        return result;
    }

    void clear() throws IOException {
        files.clear();
        flush();
    }

    private void flush() throws IOException {
        if (filePath != null) {
            VCSFiles.writeObject(filePath, files);
        }
    }
}
