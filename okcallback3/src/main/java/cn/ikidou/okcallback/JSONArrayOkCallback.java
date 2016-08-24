package cn.ikidou.okcallback;

import org.json.JSONArray;
import org.json.JSONTokener;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by chenjian on 16-8-24.
 */
public abstract class JSONArrayOkCallback extends OkCallback<JSONArray> {
    @Override
    protected JSONArray convert(Call call, Response response, Type type) throws Exception {
        return new JSONArray(new JSONTokener(response.body().charStream()));
    }
}
