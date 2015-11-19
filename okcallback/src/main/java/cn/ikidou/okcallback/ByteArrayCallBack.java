package cn.ikidou.okcallback;

import com.squareup.okhttp.Response;

/**
 * 该类继承自 OkCallBack<byte[]>，自动将结果转为byte[]类型
 *
 * @author 怪盗kidou bestkidou@gmail.com
 * @see OkCallBack
 * @see FileCallBack
 * @see GsonCallBack
 * @see JSONArrayCallBack
 * @see JSONObjectCallBack
 * @since 1.0.0
 */
public abstract class ByteArrayCallBack extends OkCallBack<byte[]> {
    /**
     * 实现响应到 byte[] 的转换
     *
     * @param response OkHttp Response 服务器响应
     * @return Result 转换后的结果
     * @throws Exception
     */
    @Override
    protected final byte[] convert(Response response) throws Exception {
        byte[] bytes = response.body().bytes();
        return bytes;
    }
}
