package ca.uwaterloo.newsapp.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ca.uwaterloo.newsapp.Entity.User;
import ca.uwaterloo.newsapp.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Headers;



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


    public String get(String url, String token) throws IOException {
        String result = null;
        Response response = null;
        Request request = new Request.Builder()
                .url(host+url)
                .headers(SetHeaders(token))
                .build();
        Call call = client.newCall(request);
        try {
             response = call.execute();
            if(response.code() == HttpCode.SUCCESS){
                String string = response.body().string();
                JSONObject object = null;
                try {
                    object = new JSONObject(string);
                    result = object.getString("username");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            return null;
        }finally {
            if(response != null){
                response.body().close();
            }
        }
        return result;
    }

    public static Headers SetHeaders(String token) {
        Headers headers = null;
        okhttp3.Headers.Builder headersbuilder = new okhttp3.Headers.Builder();
        headersbuilder.add("token", token);
        headers = headersbuilder.build();
        return headers;
    }


    public static void main(String[] args) throws IOException {
        User user = new User("alice","alice");
        String b = JsonUtil.toJson(user);
        HttpUtils httpUtils = new HttpUtils();
        ResponseBody r = httpUtils.post("/api/v1/login",b);
        System.out.println(r.getBody());

    }
}
