package cn.ikidou.okcallback.dispatcher;

import android.os.Handler;
import android.os.Looper;

/**
 * Android环境下的分发器，使得被调度的方法运行在UI线程中
 *
 * @since 1.0.0
 */
class DefaultAndroidDispatcher extends Dispatcher {
    DefaultAndroidDispatcher() {
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void dispatch(Runnable runnable) {
        mHandler.post(runnable);
    }

}
