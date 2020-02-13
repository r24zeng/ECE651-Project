package ca.uwaterloo.newsapp.utils;

public class ResponseBody {
    private int code;
    private String body;

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }

    public ResponseBody(int code, String body) {
        this.code = code;
        this.body = body;
    }

}
