package General;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Methods {
    public static String hasher(String string) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytesPassword = new byte[0];
        MessageDigest md = null;

        bytesPassword = string.getBytes("UTF-8");
        md = MessageDigest.getInstance("MD5");
        byte[] hashedPassword = md.digest(bytesPassword);
        String returnPassword = new String(hashedPassword);
        return returnPassword;
    }
}
