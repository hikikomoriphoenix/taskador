package marabillas.loremar.taskador.network;

/**
 * Holds contents of the response from the back-end server.
 */
public class BackEndResponse {
    private int statusCode;
    private String contentType;
    private String data;

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public String getData() {
        return data;
    }
}
