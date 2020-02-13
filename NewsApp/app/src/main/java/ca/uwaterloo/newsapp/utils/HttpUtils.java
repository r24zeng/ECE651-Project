package ca.uwaterloo.newsapp.utils;

import java.io.IOException;

import ca.uwaterloo.newsapp.Entity.User;
import ca.uwaterloo.newsapp.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String host = "http:66.112.218.89:5000";
    OkHttpClient client = new OkHttpClient();

    public ResponseBody post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(host+url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = new ResponseBody(response.code(),response.body().string());
            return responseBody;
        }catch (IOException e){
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        User user = new User("alice","alice");
        String b = JsonUtil.toJson(user);
        HttpUtils httpUtils = new HttpUtils();
        ResponseBody r = httpUtils.post("/api/v1/login",b);
        System.out.println(r.getBody());
    }
}
