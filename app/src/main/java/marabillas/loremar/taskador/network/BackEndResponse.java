package marabillas.loremar.taskador.network;

public class BackEndResponse {
    private int statusCode;
    private String contentType;
    //private JSON responseData;

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }
}
