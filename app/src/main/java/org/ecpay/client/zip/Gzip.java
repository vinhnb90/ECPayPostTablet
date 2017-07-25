package org.ecpay.client.zip;

import org.apache.commons.io.IOUtils;
import org.util.binary.CharEncoding;
import org.util.convert.TotalConverter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Joe on 5/28/2017.
 */
public class Gzip {

    public static String compress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream zos = new GZIPOutputStream(baos);
            zos.write(str.getBytes(CharEncoding.UTF_8));
            zos.flush();
            zos.close();
            IOUtils.closeQuietly(zos);
            byte[] bytes = baos.toByteArray();

            return TotalConverter.toStringBase64(bytes);
        } catch (IOException ex) {
            return "IOE";
        }
    }

    public static String compress(byte[] input) {
        if (input == null || input.length == 0) {
            return "";
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream zos = new GZIPOutputStream(baos);
            zos.write(input);
            zos.flush();
            zos.close();
            IOUtils.closeQuietly(zos);
            byte[] bytes = baos.toByteArray();

            return TotalConverter.toStringBase64(bytes);
        } catch (IOException ex) {
            return "IOE";
        }
    }

    public static String decompress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(TotalConverter.toByteArrayBase64(str));
            GZIPInputStream zis = new GZIPInputStream(bais);

            return IOUtils.toString(zis, CharEncoding.UTF_8);
        } catch (IOException ex) {
            return "IOE";
        }
    }
}
