package cn.ikidou.okcallback.dispatcher;

/**
 * Java 环境下的调度器，使得被调度的方法运行在主线程中
 * @since 1.0.0
 */
class DefaultJavaDispatcher extends Dispatcher {
    DefaultJavaDispatcher() {
    }

    @Override
    public void dispatch(Runnable runnable) {
        runnable.run();
    }

}
