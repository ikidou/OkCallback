package cn.ikidou.okcallback.dispatcher;

import android.os.Handler;
import android.os.Looper;

/**
 * Android环境下的调度器，使得被调度的方法运行在主线程中
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
