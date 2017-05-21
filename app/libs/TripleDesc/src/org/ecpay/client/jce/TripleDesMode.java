package org.ecpay.client.jce;

/**
 * Created by NhatNV on Aug 19, 2014 12:52 PM.
 */
public enum TripleDesMode {
  DESede_CBC_NoPadding("DESede", "DESede/CBC/NoPadding"),
  DESede_CBC_PKCS5Padding("DESede", "DESede/CBC/PKCS5Padding"),
  DESede_ECB_NoPadding("DESede", "DESede/ECB/NoPadding"),
  DESede_ECB_PKCS5Padding("DESede", "DESede/ECB/PKCS5Padding"),
  DESede_CFB_NoPadding("DESede", "DESede/CFB/NoPadding"),
  DESede_CFB_PKCS5Padding("DESede", "DESede/CFB/PKCS5Padding"),
  DESede_OFB_NoPadding("DESede", "DESede/OFB/NoPadding"),
  DESede_OFB_PKCS5Padding("DESede", "DESede/OFB/PKCS5Padding"),;

  public String algorithm;
  public String value;

  TripleDesMode(String algorithm, String value) {
    this.algorithm = algorithm;
    this.value = value;
  }
}
