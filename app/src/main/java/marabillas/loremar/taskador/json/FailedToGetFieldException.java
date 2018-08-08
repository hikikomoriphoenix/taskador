package marabillas.loremar.taskador.json;

/**
 * This exception is thrown when the required field could not be obtained from a parsed data
 */
public class FailedToGetFieldException extends Exception {
    public FailedToGetFieldException() {
    }

    public FailedToGetFieldException(String message) {
        super(message);
    }

    public FailedToGetFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToGetFieldException(Throwable cause) {
        super(cause);
    }
}