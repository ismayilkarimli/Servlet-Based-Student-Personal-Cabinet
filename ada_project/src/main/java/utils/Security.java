package utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Security {

    public static String hashPassword(String password) {
        char[] passwordBytes = password.toCharArray();
        String salt = "%T9hrDkM$D246J6cL$rmfyp*r$P54rTf3UH&zVZys4@QACy7KZnyrmDUozeTHF$z";
        final int iterations = 80;
        final int keyLength = 128;
        byte[] saltBytes = salt.getBytes();
        SecretKey key = null;

        try {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(passwordBytes, saltBytes, iterations, keyLength);
            key = secretKeyFactory.generateSecret(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        if (key != null)
            return Hex.encodeHexString(key.getEncoded());
        else
            return password;
    }
}
