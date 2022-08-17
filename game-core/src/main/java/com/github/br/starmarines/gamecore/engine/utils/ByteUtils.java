package com.github.br.starmarines.gamecore.engine.utils;

public final class ByteUtils {

    private ByteUtils() {
    }

    public static short toUnsignedShort(byte b1, byte b2) {
        return (short) ((toUnsignedShort(b1) << 8) + toUnsignedShort(b2));
    }

    private static short toUnsignedShort(byte b) {
        return b < 0 ? (short) (0xFF + b + 1) : b;
    }

    public static int toUnsignedInt(byte b1, byte b2, byte b3, byte b4) {
        return ((toUnsignedInt(b1) << 24)) +
                   ((toUnsignedInt(b2) << 16)) +
                   ((toUnsignedInt(b3) << 8)) +
                   toUnsignedInt(b4);
    }

    private static int toUnsignedInt(byte b) {
        return b < 0 ? 0xFF + b + 1 : b;
    }

    /**
     * @param dest
     * @param startIndex
     * @param stopIndex
     * @param value      unsigned int
     */
    public static void fillByteArrayFromUnsignedInt(byte[] dest, int startIndex, int stopIndex, int value) {
        for (int i = startIndex; i < stopIndex; i++) {
            dest[i] = (byte) ((value >>> (stopIndex - i) * 8) & 0xFF);
        }
        dest[stopIndex] = (byte) (value & 0xFF);
    }

}
