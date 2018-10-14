package marabillas.loremar.taskador.json;

/**
 * This exception is thrown when a {@link JSONTree} can't successfully construct a JSON data
 */
public class JSONTreeException extends Exception {
    public JSONTreeException() {
        super();
    }

    public JSONTreeException(String message) {
        super(message);
    }

    public JSONTreeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONTreeException(Throwable cause) {
        super(cause);
    }
}
