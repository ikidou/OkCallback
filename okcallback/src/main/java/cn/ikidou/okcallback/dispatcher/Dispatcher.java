package cn.ikidou.okcallback.dispatcher;

/**
 * 调度器，在Java和Android两种不同的环境中，使得在Android系统中运行成UI线程，而Java则运行在调用线程。
 * @since 1.0.0
 * @serial
 */
public abstract class Dispatcher {
    private static final Dispatcher M_DISPATCHER;

    static {
        Dispatcher dispatcher;
        try {
            Class.forName("android.os.Looper");
            Class.forName("android.os.Handler");
            dispatcher = new DefaultAndroidDispatcher();
        } catch (Exception e) {
            dispatcher = new DefaultJavaDispatcher();
        }
        M_DISPATCHER = dispatcher;
    }

    public abstract void dispatch(Runnable runnable);
    public static Dispatcher getDefault() {
        return M_DISPATCHER;
    }

}
