package SunSideMiningStation.Services;

public class JsonResponse {
    public enum StatusCode {
        OK,
        INTERNAL_SERVER_ERROR
    }

    private static int translateStatusCodeToNumber(StatusCode code) {
        switch (code) {
            case OK:
                return 200;
            case INTERNAL_SERVER_ERROR:
                return 500;
            default:
                throw new IllegalArgumentException("Invalid code passed to status code translator");
        }
    }

    private int _statusCode;
    private String _message;

    public int getStatusCode() {
        return _statusCode;
    }

    public String getMessage() {
        return _message;
    }

    JsonResponse(int statusCode, String message) {
        _statusCode = statusCode;
        _message = message;
    }

    public JsonResponse(int statusCode) {
        this(statusCode, "No message");
    }

    JsonResponse(StatusCode statusCode, String message) {
        this(translateStatusCodeToNumber(statusCode), message);
    }

    JsonResponse(StatusCode statusCode) {
        this(statusCode, "No message");
    }
}
