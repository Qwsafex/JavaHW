package vcs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


class Blob extends GitObject {
    private String path;
    private byte[] content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Blob blob = (Blob) o;

        return path.equals(blob.path) && Arrays.equals(content, blob.content);

    }

    @Override
    public int hashCode() {
        return getSHA().hashCode();
    }

    @Override
    public String getSHA(){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA1);
            messageDigest.update(path.getBytes());
            messageDigest.update(content);
            return byteArrayToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 hashing algorithm not implemented!");
        }
    }

    Blob(Path filePath) throws IOException {
        if (Files.notExists(filePath)) {
            throw new FileNotFoundException("File " + filePath.toString() + " not found!");
        }
        path = filePath.toString();
        content = Files.readAllBytes(filePath);
    }

    @SuppressWarnings("WeakerAccess")
    public String getPath() {
        return path;
    }

    @SuppressWarnings("WeakerAccess")
    public LightBlob getLightBlob() {
        return new LightBlob(path, getSHA());
    }

    @SuppressWarnings("WeakerAccess")
    public byte[] getContent() {
        return content;
    }
}
