package cn.ikidou.okcallback;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by chenjian on 16-8-24.
 */
public abstract class GsonOkCallback<T> extends OkCallback<T> {
    private final Gson gson;

    public GsonOkCallback() {
        gson = new Gson();
    }

    public GsonOkCallback(Gson gson) {
        this.gson = gson;
    }


    @Override
    protected T convert(Call call, Response response, Type type) throws Exception {
        return gson.fromJson(response.body().charStream(), type);
    }
}
