package cn.ikidou.okcallback;

import okhttp3.Headers;

public class Response<T> {
    private okhttp3.Response rawResponse;
    private T body;

    public Response(okhttp3.Response rawResponse, T body) {
        this.rawResponse = rawResponse;
        this.body = body;
    }

    public T body() {
        return body;
    }

    public int code() {
        return rawResponse.code();
    }

    public String message() {
        return rawResponse.message();
    }

    public Headers headers() {
        return rawResponse.headers();
    }

    public String header(String name) {
        return rawResponse.header(name);
    }

    public String header(String name, String defaultValue) {
        return rawResponse.header(name, defaultValue);
    }

    public okhttp3.Response raw() {
        return rawResponse;
    }

    public boolean isSuccessful() {
        return rawResponse.isSuccessful();
    }
}
