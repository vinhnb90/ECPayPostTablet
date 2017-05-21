package org.ecpay.client;

/**
 * Created by NhatNV on Dec 03, 2014 11:41.
 */
public class Partner {
  private String code;
  private String privateKey;
  private String publicKey;

  public Partner() {
  }

  public Partner(String code, String privateKey, String publicKey) {
    this.code = code;
    this.privateKey = privateKey;
    this.publicKey = publicKey;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getIV() {
    int length1 = privateKey.length();
    int length2 = publicKey.length();

    return privateKey.substring((length1 - 4) / 2, (length1 - 4) / 2 + 4) + publicKey.substring((length2 - 4) / 2, (length2 - 4) / 2 + 4);
  }

  public String getKEY() {
    int length1 = privateKey.length();
    int length2 = publicKey.length();

    return privateKey.substring((length1 - 12) / 2, (length1 - 12) / 2 + 12) + publicKey.substring((length2 - 12) / 2, (length2 - 12) / 2 + 12);
  }
}
