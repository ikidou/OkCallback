package cn.ikidou.okcallback;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by chenjian on 16-8-24.
 */
public abstract class StringOkCallback extends OkCallback<String> {
    @Override
    protected String convert(Call call, Response response, Type type) throws Exception {
        return response.body().string();
    }
}
