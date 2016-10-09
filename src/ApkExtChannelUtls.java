
import android.content.Context;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.ZipFile;

/**
 * Created by liheng on 16/10/8.
 * 通过渠道写入apk文件末尾然后从末尾读取实现
 */
public class ApkExtChannelUtls {

    public static String readApkExtInfo(Context ctx){
        return readApkExtInfo(ctx, ctx.getPackageCodePath());
    }

    /**获取软件apk文件末尾的自定义信息*/
    public static String readApkExtInfo(Context ctx,String apkPath){
        if(ctx==null){
            return "";
        }
        byte[] bytes = null;
        try {
            RandomAccessFile accessFile = new RandomAccessFile(new File(apkPath), "r");
            long index = accessFile.length();

            bytes = new byte[2];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);

            int contentLength = stream2Short(bytes, 0);

            bytes = new byte[contentLength];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);

            return new String(bytes, "utf-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**向apk中写入一段信息,该方法通常在服务器端设置，zipFile.getComment() 需要java1.7,android4.4之前是不支持Java7 的*/
    public static void writeApkExtInfo(File file, String comment) {
        ZipFile zipFile = null;
        ByteArrayOutputStream outputStream = null;
        RandomAccessFile accessFile = null;
        try {
            zipFile = new ZipFile(file);
            String zipComment = zipFile.getComment();
            if (!TextUtils.isEmpty(zipComment)) {
                return;
            }
            byte[] byteComment = comment.getBytes();
            outputStream = new ByteArrayOutputStream();
            outputStream.write(byteComment);
            outputStream.write(short2Stream((short) byteComment.length));
            byte[] data = outputStream.toByteArray();
            accessFile = new RandomAccessFile(file, "rw");
            accessFile.seek(file.length() - 2);
            accessFile.write(short2Stream((short) data.length));
            accessFile.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (accessFile != null) {
                    accessFile.close();
                }
            } catch (Exception e) {

            }
        }
    }
    /**
     * short转换成字节数组（小端序）
     * @return
     */
    private static short stream2Short(byte[] stream, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(stream[offset]);
        buffer.put(stream[offset + 1]);
        return buffer.getShort(0);
    }
    /**
     * 字节数组转换成short（小端序）
     * @return
     */
    private static byte[] short2Stream(short data) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(data);
        buffer.flip();
        return buffer.array();
    }
}
