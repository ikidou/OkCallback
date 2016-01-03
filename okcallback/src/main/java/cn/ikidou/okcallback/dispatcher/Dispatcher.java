package cn.ikidou.okcallback.dispatcher;

/**
 * 结果/错误分发类，在Java和Android两种不同的环境中，使得在Android系统中运行成UI线程，而Java则运行在网络请求线程。
 *
 * @since 1.0.0
 */
public abstract class Dispatcher {
    private static final Dispatcher DEFAULT_DISPATCHER = initDispatcher();

    private static Dispatcher initDispatcher() {
        try {
            Class.forName("android.os.Looper");
            Class.forName("android.os.Handler");
            return new DefaultAndroidDispatcher();
        } catch (Exception e) {
            return new DefaultJavaDispatcher();
        }
    }

    public abstract void dispatch(Runnable runnable);

    public static Dispatcher getDefault() {
        return DEFAULT_DISPATCHER;
    }

}
