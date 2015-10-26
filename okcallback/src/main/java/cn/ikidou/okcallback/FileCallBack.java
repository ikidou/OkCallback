package cn.ikidou.okcallback;

import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 该类继承自 OkCallBack<File>，实现自动将结果写入文件
 *
 * @author 怪盗kidou bestkidou@gmail.com
 * @see OkCallBack
 * @see GsonCallBack
 * @see JSONArrayCallBack
 * @see JSONObjectCallBack
 * @see StringCallBack
 */
public abstract class FileCallBack extends OkCallBack<File> {
    private final File mTarget;

    /**
     * 构造方法
     *
     * @param target 目标文件(夹)
     * @throws NullPointerException if <code>target == null</code>
     * @throws RuntimeException
     */
    public FileCallBack(File target) {
        if (target == null) {
            throw new NullPointerException("The target file is null");
        }
        if (!target.exists()) {
            File parent = target.getParentFile();
            if (!parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new RuntimeException("target not exists and parent Can not create : " + target.getPath());
                }
            }else if (!parent.canWrite()) {
                throw new RuntimeException("target not exists and parent Can not write : " + target.getPath());
            }
        } else if (!target.canWrite()) {
            throw new RuntimeException("Can not write file : " + target.getPath());
        }
        this.mTarget = target;

    }

    /**
     * 实现响应到 File 的转换
     *
     * @param response OkHttp Response 服务器响应
     * @return Result 转换后的结果
     * @throws IOException
     */
    @Override
    protected final File convert(Response response) throws IOException {
        InputStream inputStream = response.body().byteStream();
        OutputStream outputStream = new FileOutputStream(mTarget);
        byte[] buff = new byte[4096];
        int len;
        while ((len = inputStream.read(buff)) != -1) {
            outputStream.write(buff, 0, len);
        }
        inputStream.close();
        outputStream.close();
        return mTarget;
    }
}
