package ru.spbau.shevchenko;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class SingleThreadedCalculator implements FileChecksumCalculator {
    private static final String MD5 = "MD5";
    private static final String NO_SUCH_ALGORITHM_MD5 = "Well, there's no MD5 algorithm in Java...";

    @Override
    public byte[] calcChecksum(Path path) throws IOException {
        return calcRecursively(path);
    }

    private byte[] calcRecursively(Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            return new SingleFileChecksumCalculator().calcChecksum(path);
        }
        else {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                MessageDigest messageDigest = MessageDigest.getInstance(MD5);
                messageDigest.update(path.getFileName().toString().getBytes());
                for (Path entry: stream) {
                    messageDigest.update(calcRecursively(entry));
                }
                return messageDigest.digest();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(NO_SUCH_ALGORITHM_MD5);
            }
        }
    }


}
