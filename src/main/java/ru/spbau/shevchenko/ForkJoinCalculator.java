package ru.spbau.shevchenko;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;

class ForkJoinCalculator implements FileChecksumCalculator {
    @Override
    public byte[] calcChecksum(Path path) throws FileNotFoundException {
        return new ForkJoinPool().invoke(new ChecksumTask(path));
    }

}
