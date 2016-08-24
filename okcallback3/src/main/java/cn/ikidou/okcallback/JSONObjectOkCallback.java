package cn.ikidou.okcallback;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by chenjian on 16-8-24.
 */
public abstract class JSONObjectOkCallback extends OkCallback<JSONObject> {
    @Override
    protected JSONObject convert(Call call, Response response, Type type) throws Exception {
        return new JSONObject(new JSONTokener(response.body().charStream()));
    }
}
