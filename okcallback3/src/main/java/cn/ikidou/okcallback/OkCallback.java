package cn.ikidou.okcallback;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.ikidou.okcallback.dispatcher.Dispatcher;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 *
 */
public abstract class OkCallback<T> implements Callback {
    private Type type;

    public OkCallback() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("No Generic Found!");
        } else {
            type = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        }

        Dispatcher.getDefault().dispatch(new Runnable() {
            @Override
            public void run() {
                onStart();
            }
        });
    }

    @Override
    public final void onResponse(Call call, Response response) {
        try {
            T body = convert(call, response, type);
            final cn.ikidou.okcallback.Response<T> result = new cn.ikidou.okcallback.Response<>(response, body);
            Dispatcher.getDefault().dispatch(new Runnable() {
                @Override
                public void run() {
                    onSuccess(call, result);
                    onStop();
                }
            });
        } catch (final Exception e) {
            Dispatcher.getDefault().dispatch(new Runnable() {
                @Override
                public void run() {
                    onError(call, e);
                }
            });
        }
    }

    protected abstract T convert(Call call, Response response, Type type) throws Exception;


    @Override
    public final void onFailure(Call call, IOException e) {
        Dispatcher.getDefault().dispatch(new Runnable() {
            @Override
            public void run() {
                onError(call, e);
                onStop();
            }
        });
    }

    public void onStart() {
    }

    public abstract void onSuccess(Call call, cn.ikidou.okcallback.Response<T> response);

    public void onError(Call call, Exception e) {
    }

    public void onStop() {
    }
}
