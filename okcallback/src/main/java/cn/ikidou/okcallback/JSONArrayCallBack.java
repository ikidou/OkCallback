package cn.ikidou.okcallback;

import com.squareup.okhttp.Response;

import org.json.JSONArray;

/**
 * 该类继承自 OkCallBack<JSONArray>，自动将结果转为JSONArray类型
 *
 * @author 怪盗kidou bestkidou@gmail.com
 * @see OkCallBack
 * @see FileCallBack
 * @see GsonCallBack
 * @see JSONObjectCallBack
 * @see StringCallBack
 * @since 1.0.0
 */
public abstract class JSONArrayCallBack extends OkCallBack<JSONArray> {

    /**
     * 实现响应到 JSONArray 的转换
     *
     * @param response OkHttp Response 服务器响应
     * @return Result 转换后的结果
     * @throws Exception
     */
    @Override
    protected JSONArray convert(Response response) throws Exception {
        String string = response.body().string();
        JSONArray array = new JSONArray(string);
        return array;
    }
}
