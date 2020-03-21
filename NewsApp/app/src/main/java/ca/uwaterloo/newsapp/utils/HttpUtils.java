package ca.uwaterloo.newsapp.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.uwaterloo.newsapp.Entity.News;
import ca.uwaterloo.newsapp.Entity.User;
import ca.uwaterloo.newsapp.R;
import okhttp3.HttpUrl;
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
    Gson gson = new Gson();
    public ResponseBody post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(host + url)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            ResponseBody responseBody = new ResponseBody(response.code(), response.body().string());
            return responseBody;
        } catch (IOException e) {
            Log.d("post", "call execute failed", e);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
        return null;
    }

    public List<News> getNews(String url) {
        List<News> result = new ArrayList<>();
        Response response = null;
        Request request = new Request.Builder()
                .url(host + url)
                .build();
        Call call = client.newCall(request);
        try {
            response = call.execute();
            if (response.code() == HttpCode.SUCCESS) {
                String string = response.body().string();
                JsonArray array = new JsonParser().parse(string).getAsJsonObject().getAsJsonArray("news");
                for(JsonElement obj : array ){
                    News news = gson.fromJson( obj , News.class);
                    result.add(news);
                }
            }
        } catch (IOException e) {
            Log.d("get", "call execute failed", e);
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
        return result;
    }

    public String get(String url, String token) {
        String result = null;
        Response response = null;
        HttpUrl httpUrl = new HttpUrl.Builder()
                .addQueryParameter("page","1")
                .host(host + url)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .headers(SetHeaders(token))
                .build();
        Call call = client.newCall(request);
        try {
            response = call.execute();
            if (response.code() == HttpCode.SUCCESS) {
                String string = response.body().string();
                JSONObject object = null;
                try {
                    object = new JSONObject(string);
                    result = object.getString("username");
                } catch (JSONException e) {
                    Log.d("get", "convert json failed", e);
                }

            }
        } catch (IOException e) {
            Log.d("get", "call execute failed", e);
        } finally {
            if (response != null) {
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

    public static void main(String[] args) {
        HttpUtils httpUtils = new HttpUtils();
        List<News> r = httpUtils.getNews("/api/v1/news/1");
        System.out.println(r.size());
    }
}
