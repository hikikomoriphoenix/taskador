package marabillas.loremar.taskador.utils;

/**
 * Static methods for operation using regular expressions.
 */
public final class RegexUtils {
    private RegexUtils() {
    }

    public static boolean validateUsername(String username) {
        return username.matches("^[\\p{L}0-9]{1,16}$");
    }

    public static boolean validatePassword(String password) {
        return password.matches("^[\\S]{6,16}$");
    }
}
