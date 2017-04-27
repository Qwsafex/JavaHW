package utils;

import java.nio.ByteBuffer;

/**
 * Class that provides utility functions for work with byte arrays.
 */
public class ByteUtils {
    private static final ByteBuffer longBuffer = ByteBuffer.allocate(Long.BYTES);
    private static final ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);

    /**
     * Converts long to byte array.
     * @param x number to convert
     * @return byte representation of given number
     */
    @SuppressWarnings("WeakerAccess")
    public static byte[] longToBytes(long x) {
        longBuffer.putLong(0, x);
        longBuffer.clear();
        return longBuffer.array().clone();
    }

    /**
     * Converts byte to byte array.
     * @param x byte to convert
     * @return byte array consisting of single given byte
     */
    public static byte[] byteToBytes(byte x) {
        byteBuffer.put(x);
        byteBuffer.clear();
        return byteBuffer.array().clone();
    }
}