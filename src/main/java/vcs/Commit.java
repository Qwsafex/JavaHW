package vcs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Commit implements GitObject {

    private static final Path COMMIT_DIR = Paths.get("commits");
    private List<ContentlessBlob> files;
    private CommitSHARef prevCommit;
    private long time;
    private String message;

    Commit(String message, List<ContentlessBlob> files, CommitSHARef prevCommit) throws IOException {
        this.message = message;
        this.files = files;
        this.time = System.currentTimeMillis();
        this.prevCommit = prevCommit;
        VCSFiles.writeObject(COMMIT_DIR.resolve(getSHA()), this);
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    CommitSHARef getPrevCommit() {
        return prevCommit;
    }

    private String getSHA() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA1);
            messageDigest.update(String.valueOf(time).getBytes());
            messageDigest.update(message.getBytes());
            return GitObject.byteArrayToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 hashing algorithm not implemented!");
        }
    }

    public static Commit get(String revision) throws IOException, ClassNotFoundException {
        return (Commit) VCSFiles.readObject(COMMIT_DIR.resolve(revision));
    }

    @Override
    public CommitSHARef getSHARef() {
        return new CommitSHARef(getSHA());
    }

    List<ContentlessBlob> getFiles() {
        return files;
    }

}
