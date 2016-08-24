package cn.ikidou.okcallback.dispatcher;

/**
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
