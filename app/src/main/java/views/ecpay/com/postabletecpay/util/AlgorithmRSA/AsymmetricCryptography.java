package views.ecpay.com.postabletecpay.util.AlgorithmRSA;

import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

//import org.apache.commons.codec.binary.Base64;
import views.ecpay.com.postabletecpay.util.commons.Common;
import android.util.Base64;
/**
 * Created by VinhNB on 5/12/2017.
 */

public class AsymmetricCryptography {
    private Cipher cipher;

    public AsymmetricCryptography() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.cipher = Cipher.getInstance("RSA");
    }

    // https://docs.oracle.com/javase/8/docs/api/java/security/spec/PKCS8EncodedKeySpec.html
    public PrivateKey getPrivate(String privateKeyString) throws Exception {
        byte[] priKeyBytes = Base64.decode(privateKeyString.getBytes("utf-8"), Base64.DEFAULT);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(priKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


    // https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html
    public PublicKey getPublic(String publicKeyString) throws Exception {
        byte[] priKeyBytes = Base64.decode(publicKeyString.getBytes("utf-8"), Base64.DEFAULT);
        byte[] keyBytes = Common.decodeBase64(publicKeyString);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /*public void encryptFile(byte[] input, File output, PrivateKey key)
            throws IOException, GeneralSecurityException {
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        writeToFile(output, this.cipher.doFinal(input));
    }

    public void decryptFile(byte[] input, File output, PublicKey key)
            throws IOException, GeneralSecurityException {
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        writeToFile(output, this.cipher.doFinal(input));
    }

    private void writeToFile(File output, byte[] toWrite)
            throws IllegalBlockSizeException, BadPaddingException, IOException {
        FileOutputStream fos = new FileOutputStream(output);
        fos.write(toWrite);
        fos.flush();
        fos.close();
    }
*/
    public String encryptTextPublickey(String msg, PublicKey key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {

        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(msg.getBytes("UTF-8"));
        return Base64.encodeToString(result,Base64.DEFAULT);
    }

    public String encryptText(String msg, PrivateKey key)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {

        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(msg.getBytes("UTF-8"));
        return Base64.encodeToString(result,Base64.DEFAULT);
    }

 /*   public static String encryptText(String text, String pri_key) {
        try {
            byte[] data = text.getBytes("utf-8");
            PublicKey publicKey = getPrivate(Base64.decode(pri_key.getBytes("utf-8"), Base64.DEFAULT));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeToString(cipher.doFinal(data),Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

   /* public static String decryptData(String text, String pub_key) {
        try {
            byte[] data =Base64.decode(text,Base64.DEFAULT);
            PrivateKey privateKey = getPrivateKey(Base64.decode(pub_key.getBytes("utf-8"),Base64.DEFAULT));

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(data),"utf-8");
        } catch (Exception e) {
            return null;
        }
    }*/

    public String decryptText(String msg, PublicKey key)
            throws InvalidKeyException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException {

        byte[] data =Base64.decode(msg,Base64.DEFAULT);
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(data), "UTF-8");
    }
    public String decryptTextPrivatekey(String msg, PrivateKey key)
            throws InvalidKeyException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException {

        byte[] data =Base64.decode(msg,Base64.DEFAULT);
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(data), "UTF-8");
    }





/*
    public byte[] getFileInBytes(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] fbytes = new byte[(int) f.length()];

        fis.read(fbytes);
        fis.close();

        return fbytes;
    }*/
/*
    public static void main(String[] args) throws Exception {
        AsymmetricCryptography ac = new AsymmetricCryptography();
        PrivateKey privateKey = ac.getPrivate("KeyPair/privateKey");
        PublicKey publicKey = ac.getPublic("KeyPair/publicKey");

        String msg = "Cryptography is fun!";
        String encrypted_msg = ac.encryptText(msg, privateKey);
        String decrypted_msg = ac.decryptText(encrypted_msg, publicKey);
        System.out.println("Original Message: " + msg +
                "\nEncrypted Message: " + encrypted_msg
                + "\nDecrypted Message: " + decrypted_msg);

        if (new File("KeyPair/text.txt").exists()) {
            ac.encryptFile(ac.getFileInBytes(new File("KeyPair/text.txt")),
                    new File("KeyPair/text_encrypted.txt"),privateKey);
            ac.decryptFile(ac.getFileInBytes(new File("KeyPair/text_encrypted.txt")),
                    new File("KeyPair/text_decrypted.txt"), publicKey);
        } else {
            System.out.println("Create a file text.txt under folder KeyPair");
        }
    }*/
}
