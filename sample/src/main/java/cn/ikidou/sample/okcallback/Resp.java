package cn.ikidou.sample.okcallback;

/**
 * Created by ikidou on 15-10-12.
 */
public class Resp<T> {
    public int code;
    public String message;
    public T data;

    @Override
    public String toString() {
        return "Resp{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
