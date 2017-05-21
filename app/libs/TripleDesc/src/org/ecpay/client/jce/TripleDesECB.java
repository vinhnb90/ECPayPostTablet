package org.ecpay.client.jce;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * Created by NhatNV on Aug 19, 2014 1:10 PM.
 */
public class TripleDesECB implements Crypto {
  Cipher cipherE, cipherD;

  public TripleDesECB(byte[] key, boolean padding) throws Exception {
    if (key != null && key.length == 24) {
      DESedeKeySpec keyspec = new DESedeKeySpec(key);
      SecretKeyFactory fac = SecretKeyFactory.getInstance(TripleDesMode.DESede_ECB_PKCS5Padding.algorithm);
      SecretKey keyDES = fac.generateSecret(keyspec);
      if (padding) {
        cipherD = Cipher.getInstance(TripleDesMode.DESede_ECB_PKCS5Padding.value);
        cipherD.init(Cipher.DECRYPT_MODE, keyDES);

        cipherE = Cipher.getInstance(TripleDesMode.DESede_ECB_PKCS5Padding.value);
        cipherE.init(Cipher.ENCRYPT_MODE, keyDES);
      } else {
        cipherD = Cipher.getInstance(TripleDesMode.DESede_ECB_NoPadding.value);
        cipherD.init(Cipher.DECRYPT_MODE, keyDES);

        cipherE = Cipher.getInstance(TripleDesMode.DESede_ECB_NoPadding.value);
        cipherE.init(Cipher.ENCRYPT_MODE, keyDES);
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
