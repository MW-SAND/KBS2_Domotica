package General;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Methods {
    public static String hasher(String string) throws NoSuchAlgorithmException {
        byte[] bytesPassword;
        MessageDigest md;

        bytesPassword = string.getBytes(StandardCharsets.UTF_8);
        md = MessageDigest.getInstance("MD5");
        byte[] hashedPassword = md.digest(bytesPassword);
        return new String(hashedPassword);
    }
}
