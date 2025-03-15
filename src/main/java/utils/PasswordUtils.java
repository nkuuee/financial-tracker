package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    public static boolean checkPassword(String password, String storedHash) {
        if (password == null || storedHash == null) {
            throw new IllegalArgumentException("Пароль или хэш не могут быть пустыми");
        }
        return BCrypt.checkpw(password, storedHash);
    }
}
