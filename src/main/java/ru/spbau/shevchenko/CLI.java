package ru.spbau.shevchenko;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class CLI {
    public static void main(String[] args) throws IOException {
        for (String path : args) {
            processSingleThreaded(path);
            processMultiThreaded(path);
        }
    }

    private static void processMultiThreaded(String path) throws IOException {
        System.out.println("Multithreaded:");
        process(path, new ForkJoinCalculator());
    }
    private static void processSingleThreaded(String path) throws IOException {
        System.out.println("Singlethreaded:");
        process(path, new SingleThreadedCalculator());
    }

    private static void process(String path, FileChecksumCalculator calculator) throws IOException {
        long startTime = System.currentTimeMillis();
        byte[] hash = calculator.calcChecksum(Paths.get(path));
        long endTime = System.currentTimeMillis();
        Double tookSeconds = (endTime - startTime) / 1000.0;
        System.out.println("Calculated result for " + path + " in " + tookSeconds.toString());
        System.out.println("Result is: " + Arrays.toString(hash));
        System.out.println();
    }

}
