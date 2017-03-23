package vcs;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Commit extends GitObject{
    private final long time;
    private final String message;
    private final List<LightBlob> blobs;
    private final String prevCommit;

    @SuppressWarnings("WeakerAccess")
    public Commit(String message, List<LightBlob> blobs, String prevCommit) {
        this.message = message;
        this.blobs = blobs;
        this.prevCommit = prevCommit;
        this.time = System.currentTimeMillis();
    }

    @Override
    public String getSHA() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(time);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA1);
            messageDigest.update(buffer.array());
            messageDigest.update(message.getBytes());
            return byteArrayToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 hashing algorithm not implemented!");
        }

    }

    @SuppressWarnings({"StringBufferReplaceableByString", "StringConcatenationInsideStringBufferAppend"})
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        result.append("Date: " + dateFormat.format(new Date(time)) + "\n");
        result.append("SHA: " + getSHA() + "\n");
        result.append("Message: " + message + "\n");


        return result.toString();
    }

    @SuppressWarnings("WeakerAccess")
    public String getPrevCommit() {
        return prevCommit;
    }

    @SuppressWarnings("WeakerAccess")
    public List<LightBlob> getBlobs() {
        return blobs;
    }

    @SuppressWarnings("WeakerAccess")
    public String getMessage() {
        return message;
    }
}
