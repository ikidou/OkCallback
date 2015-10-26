package cn.ikidou.okcallback;

import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 该类继承自 OkCallBack<JSONObject>，自动将结果转为JSONObject类型
 *
 * @author 怪盗kidou bestkidou@gmail.com
 * @see OkCallBack
 * @see FileCallBack
 * @see GsonCallBack
 * @see JSONArrayCallBack
 * @see StringCallBack
 */
public abstract class JSONObjectCallBack extends OkCallBack<JSONObject> {

    /**
     * 实现响应到 JSONObject 的转换
     *
     * @param response OkHttp Response 服务器响应
     * @return Result 转换后的结果
     * @throws IOException
     */
    @Override
    protected final JSONObject convert(Response response) throws IOException {
        String string = response.body().string();
        try {
            JSONObject object = new JSONObject(string);
            return object;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
