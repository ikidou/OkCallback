package cn.ikidou.okcallback;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 该类继承自 OkCallBack<String>，自动将结果转为String类型
 *
 * @author 怪盗kidou bestkidou@gmail.com
 * @see OkCallBack
 * @see FileCallBack
 * @see GsonCallBack
 * @see JSONArrayCallBack
 * @see JSONObjectCallBack
 */
public abstract class StringCallBack extends OkCallBack<String> {
    /**
     * 实现响应到 String 的转换
     *
     * @param response OkHttp Response 服务器响应
     * @return Result 转换后的结果
     * @throws IOException
     */
    @Override
    protected final String convert(Response response) throws IOException {
        String string = response.body().string();
        return string;
    }
}
