package cn.ikidou.okcallback;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cn.ikidou.okcallback.dispatcher.Dispatcher;


/**
 * Callback 的抽像实现类，可以指定范型，子类在{@link #convert(Response)}方法中实现 Http的响应与结果的转换。<br/>
 * 如果正常回调并实现转换结果会在 {@link #onSuccess(Headers, Object)} 抽像方法中以参数的形式传递。<br/>
 * 如果出现错误会回调 {@link #onError(int, Request, Exception)} 抽像方法。<br/>
 * 该超类还提供了 {@link #afterAll(boolean)} 、{@link #checkCode()}、HANDLER常量 帮助提供程序的灵活性。<br/>
 *
 * @param <T> 目标结果类型
 * @author 怪盗kidou bestkidou@gmail.com
 * @see FileCallBack
 * @see GsonCallBack
 * @see JSONArrayCallBack
 * @see JSONObjectCallBack
 * @see StringCallBack
 */
public abstract class OkCallBack<T> implements Callback {
    private int responseCode;

    /**
     * 实现Callback.onResponse，当远程服务器正常返回数据时回调该方法<br/>
     * 该方法会并调用抽象方法{@link #convert(Response)}以子类实现数据类型转换<br/>
     *
     * @param response OkHttp响应
     * @throws IOException
     */
    public final void onResponse(final Response response) throws IOException {
        responseCode = response.code();
        if (checkCode() && responseCode >= 400) {
            final String s = response.body().string();
            dispatchError(response.request(), new Exception("code:" + responseCode + " body:" + s));
            return;
        }

        try {
            final T res = convert(response);
            dispatchSuccess(response.headers(), res);
        } catch (final Exception e) {
            dispatchError(response.request(), e);
        }

    }

    /**
     * 分配当遇到错误时的回调
     *
     * @param request Okhttp request
     * @param e
     * @see #onError(int, Request, Exception)
     */
    private void dispatchError(final Request request, final Exception e) {
        Dispatcher.getDefault().dispatch(new Runnable() {
            @Override
            public void run() {
                onError(responseCode, request, e);
                afterAll(false);
            }
        });
    }

    /**
     * 分配正常执行完成时的回调
     *
     * @param headers
     * @param res
     * @see #onSuccess(Headers, Object)
     */
    private void dispatchSuccess(final Headers headers, final T res) {
        Dispatcher.getDefault().dispatch(new Runnable() {
            @Override
            public void run() {
                onSuccess(headers, res);
                afterAll(true);
            }
        });
    }

    /**
     * 实现Callback.onFailure 方法,该方法只在请求发送失败时回调
     *
     * @param request
     * @param e
     */
    @Override
    public final void onFailure(final Request request, final IOException e) {
        dispatchError(request, e);
    }

    /**
     * 类型转换抽象方法，子类在该方法中实现服务器响应的方法，该方法运行在子线程中，可以进行耗时操作
     *
     * @param response OkHttp Response 服务器响应
     * @return Result 转换后的结果
     * @throws Exception 任何的Exception抛出都会回调到{@link #onError(int, Request, Exception)}
     */
    protected abstract T convert(Response response) throws Exception;

    /**
     * 正确的结果回调,Android 环境下在UI线程中执行，Java环境中在调用线程执行
     *
     * @param headers http headers
     * @param result  the final result
     */
    public abstract void onSuccess(Headers headers, T result);

    /**
     * 当出错时回调该方法，包括：<br/>
     * 1、发送请求失败<br/>
     * 2、{@link #checkCode()} && code >= 400<br/>
     * 3、{@link #convert(Response)} 中 抛出Exception<br/>
     * 如果需要自行处理 状态码，即 code >= 400 的情况不回调该方法，请覆盖 {@link #checkCode()}
     * <p/>
     * Android 环境下在UI线程中执行，Java环境中在调用线程执行
     *
     * @param code    HTTP响应码，如果未连接到服务器时，该值为0
     * @param request
     * @param e       异常信息
     * @see #checkCode()
     */
    public abstract void onError(int code, Request request, Exception e);

    /**
     * 该方法会被 {@link #onResponse(Response)} 调用以确定是否检查 httpCode >=400 ，默认值 <b>true</b>
     *
     * @return 是否检查 code >= 400 的情况，如需自行处理，返回 <b>false</b>
     */
    protected boolean checkCode() {
        return true;
    }

    /**
     * 该方法将会在{@link #onSuccess(Headers, Object)} 或 {@link #onError(int, Request, Exception)} 后回调
     * Android 环境下在UI线程中执行，Java环境中在调用线程执行
     *
     * @param successful 当成功回调时为<code>true</code>，如果出现错误则为<code>false</code>
     */
    protected void afterAll(boolean successful) {
    }
}
