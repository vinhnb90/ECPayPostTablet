package org.ecpay.client.test;

import android.content.Context;
import android.provider.Settings;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Formatter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import views.ecpay.com.postabletecpay.R;

import static views.ecpay.com.postabletecpay.util.commons.Common.SPACE_TEXT;


/**
 * Created by Bui on 5/16/2017.
 */

public class SecurityUtils {

    /*public static String getSignUser(String... values) {
        String sign = "";
        String dataSign = "";
        for (String param : values) {
            dataSign = dataSign + param;
        }
        dataSign = dataSign + EDongApplication.getAppContext().getString(R.string.privateKey) + DateTimeUtils.dateNowFormat();
        sign = md5(md5(dataSign).toUpperCase() + EDongApplication.getAppContext().getString(R.string.publicKey)).toUpperCase();
        return sign;
    }*/

    public static String encryptRSAToString(String clearText, String publicKey) {
        String encryptedBase64 = "";
        String public_key;
        try {
            String public_key1 = publicKey.replace("-----BEGIN PUBLIC KEY-----", "");
            public_key = public_key1.replace("-----END PUBLIC KEY-----", "");
            encryptedBase64 = new String(Base64.encode(encryptWithPublicKey(clearText, public_key), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedBase64.replaceAll("(\\r|\\n)", "");
    }

    public static byte[] encryptWithPublicKey(String encrypt, String public_key) throws Exception {
        byte[] message = encrypt.getBytes("UTF-8");
        PublicKey apiPublicKey = getRSAPublicKeyFromString(public_key);
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher.init(Cipher.ENCRYPT_MODE, apiPublicKey);
        return rsaCipher.doFinal(message);
    }

    private static PublicKey getRSAPublicKeyFromString(String public_key) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] publicKeyBytes = Base64.decode(public_key.getBytes("UTF-8"), Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(x509KeySpec);
    }

    public static String getSerialNumber(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String sign(String plainText, String privateKey) throws Exception {
        String private_key;
        String private_key1 = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
        private_key = private_key1.replace("-----END PRIVATE KEY-----", "");
        byte[] encodedPrivateKey = Base64.decode(private_key, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(keySpec);
        Signature privateSignature = Signature.getInstance("SHA1withRSA");
        privateSignature.initSign(privKey);
        privateSignature.update(plainText.getBytes("UTF-8"));

        byte[] signature = privateSignature.sign();

        return new String(Base64.encode(signature, Base64.DEFAULT));
//                Base64.encodeToString(signature, Base64.DEFAULT);
    }

    public static String tripleDesc(Context context, String password, String privateKeyRSA, String publicKeyRSA) {
        //check pass: if pass length < 8 insert space
        password = password.trim();
        while (password.length() < 8) {
            password = SPACE_TEXT + password;
        }

        try {
            privateKeyRSA = context.getString(R.string.privateKey);
            publicKeyRSA = context.getString(R.string.publicKey);
            try {
                privateKeyRSA = privateKeyRSA.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").trim().replace("\n", "").replace("\r", "");
                publicKeyRSA = publicKeyRSA.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").trim().replace("\n", "").replace("\r", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] abc = getKey(privateKeyRSA, publicKeyRSA).getBytes();
            final SecretKey key = new SecretKeySpec(abc, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(getIV(privateKeyRSA, publicKeyRSA).getBytes());
            final Cipher cipher;
            cipher = Cipher.getInstance("DESede/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            final byte[] plainTextBytes = password.getBytes("UTF-8");
            final byte[] cipherText = cipher.doFinal(plainTextBytes);
            return bytesToHexString(cipherText).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return sb.toString();
    }

    public static int getAuditNumber() {
        return 1102;
    }

    public String decrypt(byte[] message) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
                .getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8; ) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, key, iv);

        // final byte[] encData = new
        // sun.misc.BASE64Decoder().decodeBuffer(message);
        final byte[] plainText = decipher.doFinal(message);

        return new String(plainText, "UTF-8");
    }

    public static String getKey(String privateKey, String publicKey) {
        int length1 = privateKey.length();
        int length2 = publicKey.length();
//        return privateKey.substring((length1 - 12) / 2, (length1 - 12) / 2 + 12) + publicKey.substring((length2 - 12) / 2, (length2 - 12) / 2 + 12);
        return privateKey.substring((privateKey.length() - 12) / 2, ((privateKey.length() - 12) / 2) + 12) + publicKey.substring((publicKey.length() - 12) / 2, ((publicKey.length() - 12) / 2) + 12);
    }

    public static String getIV(String privateKey, String publicKey) {
        int length1 = privateKey.length();
        int length2 = publicKey.length();
//        return privateKey.substring((length1 - 4) / 2, (length1 - 4) / 2 + 4) + publicKey.substring((length2 - 4) / 2, (length2 - 4) / 2 + 4);
        return privateKey.substring((privateKey.length() - 4) / 2, ((privateKey.length() - 4) / 2) + 4) + publicKey.substring((publicKey.length() - 4) / 2, ((publicKey.length() - 4) / 2) + 4);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
