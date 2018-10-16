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
}
