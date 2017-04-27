package utils;

import java.nio.ByteBuffer;

public class ByteUtils {
    private static final ByteBuffer longBuffer = ByteBuffer.allocate(Long.BYTES);
    private static final ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);

    @SuppressWarnings("WeakerAccess")
    public static byte[] longToBytes(long x) {
        longBuffer.putLong(0, x);
        longBuffer.clear();
        return longBuffer.array().clone();
    }
    public static byte[] byteToBytes(byte x) {
        byteBuffer.put(x);
        byteBuffer.clear();
        return byteBuffer.array().clone();
    }
}