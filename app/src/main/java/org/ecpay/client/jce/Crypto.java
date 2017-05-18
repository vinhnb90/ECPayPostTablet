package org.ecpay.client.jce;

/**
 * Created by NhatNV on Aug 19, 2014 12:00 PM.
 */
public interface Crypto {
  public byte[] encrypt(byte[] data) throws Exception;

  public byte[] decrypt(byte[] data) throws Exception;
}
