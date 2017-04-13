package vcs;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

class Commit implements GitObject {

    private static final Path COMMIT_DIR = Paths.get("commits");
    @NotNull
    private List<ContentlessBlob> files;
    private CommitSHARef prevCommit;
    private long time;
    private String message;

    Commit(String message, @NotNull List<ContentlessBlob> files, CommitSHARef prevCommit) throws IOException {
        this.message = message;
        this.files = files;
        this.time = System.currentTimeMillis();
        this.prevCommit = prevCommit;
        VCSFiles.writeObject(COMMIT_DIR.resolve(getSHA()), this);
    }

    @Override
    public String toString() {
        @SuppressWarnings("StringBufferReplaceableByString") StringBuilder result = new StringBuilder(getSHA() + "\n");
        result.append(message);
        return result.toString();
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

    static Commit get(String revision) throws IOException, ClassNotFoundException {
        return (Commit) VCSFiles.readObject(COMMIT_DIR.resolve(revision));
    }

    @Override
    public CommitSHARef getSHARef() {
        return new CommitSHARef(getSHA());
    }

    @NotNull
    List<ContentlessBlob> getFiles() {
        return files;
    }

}
