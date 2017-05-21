package org.ecpay.client.rsa;

import java.io.IOException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA. User: NguyenNhat Date: 7/4/11 Time: 10:19 AM To
 * change this template use File | Settings | File Templates.
 */
public class RSA {

    public static String SHA1withRSA = "SHA1withRSA";
    public static String RSA = "RSA";
    public static final String UTF_8 = "UTF-8";

    public static PrivateKey getPrivateKeyFromString(String key) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Utils.toByteArrayBase64(key));

            return keyFactory.generatePrivate(privateKeySpec);
        } catch (IOException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static PublicKey getPublicKeyFromString(String key) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Utils.toByteArrayBase64(key));

            return keyFactory.generatePublic(publicKeySpec);
        } catch (IOException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static byte[] encrypt(String msg, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(msg.getBytes(UTF_8));
    }

    public static byte[] encrypt(String msg, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(msg.getBytes(UTF_8));
    }

    public static String encryptPassFromPublicKey(String pass, String publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        return Utils.toStringBase64(encrypt(pass, getPublicKeyFromString(publicKey)));
    }

    public static String encryptPassFromPrivateKey(String pass, String privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        return Utils.toStringBase64(encrypt(pass, getPrivateKeyFromString(privateKey)));
    }

    public static String getSignatureFromPrivateKey(String dataSign, String privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, SignatureException {
        return Utils.toStringBase64(signData(dataSign.getBytes(Utils.UTF_8), getPrivateKeyFromString(privateKey)));
    }

    public static byte[] decrypt(byte[] msg, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(msg);
    }

    public static byte[] decrypt(byte[] msg, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(msg);
    }

    public static byte[] signData(byte[] toBeSigned, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        if (privateKey == null) {
            return null;
        }
        Signature rsa = Signature.getInstance(SHA1withRSA);
        rsa.initSign(privateKey);
        rsa.update(toBeSigned);

        return rsa.sign();
    }

    public static boolean verifyData(byte[] signature, byte[] data, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance(SHA1withRSA);
        sign.initVerify(publicKey);
        sign.update(data);

        return sign.verify(signature);
    }

}
