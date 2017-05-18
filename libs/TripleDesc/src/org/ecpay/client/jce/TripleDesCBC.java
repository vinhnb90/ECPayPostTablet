package org.ecpay.client.jce;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by NhatNV on Aug 19, 2014 1:24 PM.
 */
public class TripleDesCBC implements Crypto {
  Cipher cipherD, cipherE;

  public TripleDesCBC(byte[] key, byte[] iv, boolean padding) throws Exception {
    if (key != null && key.length == 24) {
      DESedeKeySpec keyspec = new DESedeKeySpec(key);
      SecretKeyFactory fac = SecretKeyFactory.getInstance(TripleDesMode.DESede_CBC_NoPadding.algorithm);
      SecretKey keyDES = fac.generateSecret(keyspec);
      if (iv != null && iv.length == 8) {
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        if (padding) {
          cipherD = Cipher.getInstance(TripleDesMode.DESede_CBC_PKCS5Padding.value);
          cipherD.init(Cipher.DECRYPT_MODE, keyDES, ivSpec);

          cipherE = Cipher.getInstance(TripleDesMode.DESede_CBC_PKCS5Padding.value);
          cipherE.init(Cipher.ENCRYPT_MODE, keyDES, ivSpec);
        } else {
          cipherD = Cipher.getInstance(TripleDesMode.DESede_CBC_NoPadding.value);
          cipherD.init(Cipher.DECRYPT_MODE, keyDES, ivSpec);

          cipherE = Cipher.getInstance(TripleDesMode.DESede_CBC_NoPadding.value);
          cipherE.init(Cipher.ENCRYPT_MODE, keyDES, ivSpec);
        }
      } else {
        throw new IllegalArgumentException("InitVector length incorrect !");
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
