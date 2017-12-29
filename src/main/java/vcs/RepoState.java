package vcs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

class RepoState {
    private HashMap<String, ContentlessBlob> files;
    private Path filePath = null;

    private RepoState() {
        files = new HashMap<>();
    }
    RepoState(String stateName) throws IOException, ClassNotFoundException {
        filePath = Paths.get(stateName);
        if (VCSFiles.exists(filePath)) {
            //noinspection unchecked
            files = (HashMap<String, ContentlessBlob>) VCSFiles.readObject(filePath);
        }
        else {
            files = new HashMap<>();
            VCSFiles.create(filePath);
            VCSFiles.writeObject(filePath, files);
        }
    }

    void add(ContentlessBlob blob) throws IOException {
        if (files.containsKey(blob.getPath())) {
            return;
        }
        files.put(blob.getPath(), blob);
        flush();
    }
    @SuppressWarnings("unused")
    public void remove(ContentlessBlob blob) throws IOException {
        if (!files.containsKey(blob)) {
            return; // TODO: throw exception?
        }
        files.remove(blob.getPath());
        flush();
    }

    boolean empty() {
        return files.isEmpty();
    }

    void updateWith(RepoState delta) throws IOException {
        for (Map.Entry<String, ContentlessBlob> entry : delta.getFiles().entrySet()) {
            files.put(entry.getKey(), entry.getValue());
        }
        flush();
    }

    HashMap<String, ContentlessBlob> getFiles() {
        return files;
    }

    static RepoState getFromCommit(Commit commit) throws IOException, ClassNotFoundException {
        RepoState result = new RepoState();
        while (commit != null) {
            List<ContentlessBlob> blobs = commit.getFiles();
            for (ContentlessBlob blob : blobs) {
                result.add(blob);
            }
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
