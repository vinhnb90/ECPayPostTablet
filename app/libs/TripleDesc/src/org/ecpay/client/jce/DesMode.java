package org.ecpay.client.jce;

/**
 * Created by NhatNV on Aug 19, 2014 12:59 PM.
 */
public enum DesMode {
  DES_CBC_NoPadding("DES", "DES/CBC/NoPadding"),
  DES_CBC_PKCS5Padding("DES", "DES/CBC/PKCS5Padding"),
  DES_ECB_NoPadding("DES", "DES/ECB/NoPadding"),
  DES_ECB_PKCS5Padding("DES", "DES/ECB/PKCS5Padding"),
  DES_CFB_NoPadding("DESede", "DESede/CFB/NoPadding"),
  DES_CFB_PKCS5Padding("DESede", "DESede/CFB/PKCS5Padding"),
  DES_OFB_NoPadding("DESede", "DESede/OFB/NoPadding"),
  DES_OFB_PKCS5Padding("DESede", "DESede/OFB/PKCS5Padding");

  public String algorithm;
  public String value;

  DesMode(String algorithm, String value) {
    this.algorithm = algorithm;
    this.value = value;
  }
}
