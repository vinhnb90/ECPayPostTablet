package org.ecpay.client;

import org.jpos.iso.ISOMsg;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NhatNV on Dec 05, 2014 14:46.
 */
public class Utils {
  public static String toString(Date date, String pattern) {
    if (date == null) {
      return null;
    } else {
      Format formatter = new SimpleDateFormat(pattern);
      return formatter.format(date);
    }
  }

  public static String toString(ISOMsg isoMsg) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i <= isoMsg.getMaxField(); i++) {
      if (isoMsg.hasField(i))
        sb.append("field_").append(i).append(" = ").append(isoMsg.getString(i)).append("\n");
    }
    return sb.toString();
  }

  static String MD5 = "MD5";
  public static String UTF8 = "UTF-8";
  static byte[] hexArray = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

  public static String encryptMD5(String val) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance(MD5);
    md.update(val.getBytes(UTF8));
    byte[] bytes = md.digest();

    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    for (byte anInput : bytes) {
      bOut.write(hexArray[anInput >>> 4 & 0x0F]);
      bOut.write(hexArray[anInput & 0x0F]);
    }
    byte[] hexs = bOut.toByteArray();

    return new String(hexs);
  }

  public static final String DATE_FORMAT = "yyyyMMdd";

  public static String getDataSign(ISOMsg isoMsg) {
    StringBuilder sb = new StringBuilder();
    try {
      isoMsg.unpack(isoMsg.pack());
      for (int i = 0; i <= isoMsg.getMaxField(); i++) {
        if (isoMsg.hasField(i) && i != 50) {
          sb.append(isoMsg.getString(i));
        }
      }
      sb.append(toString(new Date(), DATE_FORMAT));
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public static byte[] decodeHex(char[] data) throws Exception {
    int len = data.length;
    if ((len & 0x01) != 0) {
      throw new Exception("Odd number of characters.");
    }
    byte[] out = new byte[len >> 1];
    for (int i = 0, j = 0; j < len; i++) {
      int f = toDigit(data[j], j) << 4;
      j++;
      f = f | toDigit(data[j], j);
      j++;
      out[i] = (byte) (f & 0xFF);
    }

    return out;
  }

  protected static int toDigit(char ch, int index) throws Exception {
    int digit = Character.digit(ch, 16);
    if (digit == -1) {
      throw new Exception("Illegal hexadecimal charcter " + ch + " at index " + index);
    }
    return digit;
  }

  public static byte[] encodeHex(byte[] array) {
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    for (byte anInput : array) {
      bOut.write(hexArray[anInput >>> 4 & 0x0F]);
      bOut.write(hexArray[anInput & 0x0F]);
    }

    return bOut.toByteArray();
  }

  public static void main(String[] args) throws Exception {
//    System.out.println("bloh : " + new String(encodeHex("nhật".getBytes())));
//    System.out.println("blah : " + new String(decodeHex("413b3131313832333338313b31372d30372d323030373b3b4e475559454e2056414e204e4841543b4e67c3b52042652c2063e1bba56d20342c2078c3a320c490616e205068c6b0e1bba36e672c20687579e1bb876e20c490616e205068c6b0e1bba36e672c2048c3a0204ee1bb99693b3b32332d30382d31393835".toCharArray())));
//    System.out.println("PD01000000093".substring(0, 6));
//    System.out.println("PH09919032522".matches("^PH.+"));
//    String encStr = "11AE85637639379FA1E68E2F487E39162394FC6C2B4A23BE1A82FA9CD35821812B207AA8307BF19C3B6A5AE0A646945A79853005BC78DC85F06764D12100964A16171DDD685FF533C1F6BE81816AD74F";
  }
}
