package ru.spbau.shevchenko;

import java.io.IOException;
import java.nio.file.Path;

interface FileChecksumCalculator {
    byte[] calcChecksum(Path path) throws IOException;
}
