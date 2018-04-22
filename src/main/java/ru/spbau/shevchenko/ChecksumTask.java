package ru.spbau.shevchenko;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

class ChecksumTask extends RecursiveTask<byte[]> {
    private static final String MD5 = "MD5";
    private static final String NO_SUCH_ALGORITHM_MD5 = "Well, there's no MD5 algorithm in Java...";
    private Path path;

    ChecksumTask(Path path) throws FileNotFoundException {
        if (Files.notExists(path)) {
            throw new FileNotFoundException(path.toString() + " not found!");
        }
        this.path = path;
    }

    @Override
    protected byte[] compute() {
        if (!Files.isDirectory(path)) {
            try {
                return new SingleFileChecksumCalculator().calcChecksum(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to calculate checksum for " + path.toString());
            }
        }
        else {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                MessageDigest messageDigest = MessageDigest.getInstance(MD5);
                messageDigest.update(path.getFileName().toString().getBytes());
                List<ChecksumTask> subtasks = new ArrayList<>();
                for (Path entry: stream) {
                    ChecksumTask subtask = new ChecksumTask(entry);
                    subtask.fork();
                    subtasks.add(subtask);
                }
                for (ChecksumTask subtask : subtasks) {
                    messageDigest.update(subtask.join());
                }
                return messageDigest.digest();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(NO_SUCH_ALGORITHM_MD5);
            } catch (IOException e) {
                throw new RuntimeException("Failed to calculate checksum for " + path.toString());
            }
        }
    }
}
