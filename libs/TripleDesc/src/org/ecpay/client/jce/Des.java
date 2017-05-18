package org.ecpay.client.jce;

import javax.crypto.Cipher;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by NhatNV on Aug 19, 2014 12:50 PM.
 */
public class Des implements Crypto {
  Cipher cipherD, cipherE;

  public Des(byte[] key, DesMode desMode) throws Exception {
    if (key != null && key.length == 8) {
      if (DESKeySpec.isParityAdjusted(key, 0)) {
        throw new IllegalArgumentException("Key ParityAdjusted !");
      } else if (DESKeySpec.isWeak(key, 0)) {
        throw new IllegalArgumentException("Key is weak !");
      } else {
        SecretKeySpec desKey = new SecretKeySpec(key, desMode.algorithm);
        cipherD = Cipher.getInstance(desMode.value);
        cipherE = Cipher.getInstance(desMode.value);
        cipherD.init(Cipher.DECRYPT_MODE, desKey);
        cipherE.init(Cipher.ENCRYPT_MODE, desKey);
      }
    } else {
      throw new IllegalArgumentException("Key length incorrect !");
    }
  }

  @Override
  public byte[] encrypt(byte[] data) throws Exception {
    return cipherE.doFinal(data);
  }

  @Override
  public byte[] decrypt(byte[] data) throws Exception {
    return cipherD.doFinal(data);
  }
}
