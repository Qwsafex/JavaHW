package vcs;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class ContentfulBlob implements Blob{
    private String path;
    @NotNull
    private byte[] content;

    private String blobSHA;

    ContentfulBlob(Path filePath) throws IOException {
        content = Files.readAllBytes(filePath);
        path = filePath.toString();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA1);
            messageDigest.update(path.getBytes());
            messageDigest.update(content);
            blobSHA = GitObject.byteArrayToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 hashing algorithm not implemented!");
        }
        VCSFiles.writeObject(BLOB_DIR.resolve(blobSHA), this);
    }


    @Override
    public BlobSHARef getSHARef() {
        return new BlobSHARef(blobSHA);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public ContentfulBlob getContentfulBlob() {
        return this;
    }

    @Override
    public ContentlessBlob getContentlessBlob() {
        return new ContentlessBlob(path, getSHARef());
    }

    @NotNull
    byte[] getContent() {
        return content;
    }
}
