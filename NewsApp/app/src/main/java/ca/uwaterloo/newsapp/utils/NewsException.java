package ca.uwaterloo.newsapp.utils;

public class NewsException extends RuntimeException {
    private String message;

    public NewsException(String message) {
        super(message);
        this.message = message;
    }

    public NewsException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
