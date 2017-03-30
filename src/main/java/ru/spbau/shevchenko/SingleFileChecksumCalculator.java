package ru.spbau.shevchenko;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SingleFileChecksumCalculator implements FileChecksumCalculator{
    private static final String MD5 = "MD5";
    private static final String NO_SUCH_ALGORITHM_MD5 = "Well, there's no MD5 algorithm in Java...";
    private static final int CACHE_SIZE = 4096;


    @Override
    public byte[] calcChecksum(Path path) throws IOException {
        InputStream inputStream = Files.newInputStream(path);
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(MD5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(NO_SUCH_ALGORITHM_MD5);
        }
        byte[] cache = new byte[CACHE_SIZE];
        int bytesRead = CACHE_SIZE;
        while (bytesRead == CACHE_SIZE) {
            bytesRead = inputStream.read(cache, 0, CACHE_SIZE);
            messageDigest.update(cache, 0, bytesRead);
        }
        return messageDigest.digest();    }
}
