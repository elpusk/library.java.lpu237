package kr.co.elpusk.android.liblpu237.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class IntByteConvert {
    public static byte[] intTobyte(int integer, ByteOrder order) {

        ByteBuffer buff = ByteBuffer.allocate(Integer.BYTES);
        buff.order(order);
        buff.putInt(integer);
        return buff.array();
    }

    public static int byteToInt(byte[] bytes, ByteOrder order) {

        ByteBuffer buff = ByteBuffer.allocate(Integer.BYTES);
        buff.order(order);

        buff.put(bytes);
        buff.flip();

        return buff.getInt();
    }

    public static byte[] shortTobyte(short integer, ByteOrder order) {

        ByteBuffer buff = ByteBuffer.allocate(Short.BYTES);
        buff.order(order);

        buff.putShort(integer);
        return buff.array();
    }

    public static short byteToShort(byte[] bytes, ByteOrder order) {

        ByteBuffer buff = ByteBuffer.allocate(Short.BYTES);
        buff.order(order);

        buff.put(bytes);
        buff.flip();

        return buff.getShort();
    }

}
