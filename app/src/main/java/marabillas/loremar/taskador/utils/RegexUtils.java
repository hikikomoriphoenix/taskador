package marabillas.loremar.taskador.utils;

/**
 * Static methods for operation using regular expressions.
 */
public final class RegexUtils {
    private RegexUtils() {
    }

    /**
     * Check if username is 1-16 alphanumeric characters.
     *
     * @param username username to check
     * @return true if it is 1-16 alphanumeric characters. False otherwise.
     */
    public static boolean validateUsername(String username) {
        return username.matches("^[\\p{L}0-9]{1,16}$");
    }

    /**
     * Check if password is 6-16 non-whitespace characters.
     *
     * @param password password to check
     * @return true if it is 6-16 non-whitespace characters. False otherwise.
     */
    public static boolean validatePassword(String password) {
        return password.matches("^[\\S]{6,16}$");
    }
}
