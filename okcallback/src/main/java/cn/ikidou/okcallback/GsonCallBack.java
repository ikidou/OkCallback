package cn.ikidou.okcallback;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Response;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 该类继承自 OkCallBack<T>，使用GSON自动将结果转为范型中指定的类型
 *
 * @param <T> Result Type
 * @author 怪盗kidou bestkidou@gmail.com
 * @see OkCallBack
 * @see FileCallBack
 * @see JSONArrayCallBack
 * @see JSONObjectCallBack
 * @see StringCallBack
 * @since 1.0.0
 */
public abstract class GsonCallBack<T> extends OkCallBack<T> {
    /**
     * 范型信息
     */
    private final Type mType;
    /**
     * JSON 解析器
     */
    private Gson mGson;

    /**
     * 默认构造方法
     *
     * @since 1.0.0
     */
    public GsonCallBack() {
        this(null);
    }

    /**
     * 默认构造方法构造方法，支持自定义Gson解析器
     * @param gson 自定义的Gson解析器，你可以使用 {@code GsonBuilder} 来自定义
     * @see #GsonCallBack()
     * @since 1.0.0
     */
    public GsonCallBack(Gson gson) {
        this.mType = getSuperclassTypeParameter(getClass());
        this.mGson = gson;
    }

    static final Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * 实现响应到 范型类型 的转换
     *
     * @param response OkHttp Response 服务器响应
     * @return Result 转换后的结果
     * @throws Exception
     */
    @Override
    protected final T convert(Response response) throws Exception {
        Reader reader = response.body().charStream();
        if (mGson == null) {
            mGson = new Gson();
        }
        T resp = mGson.fromJson(reader, mType);
        return resp;
    }

}
