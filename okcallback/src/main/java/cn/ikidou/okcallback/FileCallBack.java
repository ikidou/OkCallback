package cn.ikidou.okcallback;

import com.squareup.okhttp.Request;
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
    private boolean noExceptionThrow;

    /**
     * 构造方法
     *
     * @param target 目标文件(夹),如果<code>target<code/>是文件夹，则创建临时文件
     * @throws NullPointerException if <code>target == null</code>
     * @throws RuntimeException
     * @see #FileCallBack(File, boolean)
     */
    public FileCallBack(File target) {
        this(target, false);
    }

    /**
     * 构造方法
     *
     * @param target           目标文件(夹)，如果<code>target<code/>是文件夹，则创建临时文件
     * @param noExceptionThrow 如果目标文件不能被写入时会,且<code>noExceptionThrow == true</code>，
     *                         则立即抛出异常，若且<code>noExceptionThrow == false</code>，不会抛出异常
     *                         而是回调 {@link #onError(int, Request, Exception)} 方法
     * @throws NullPointerException <code>target == null</code>
     * @throws RuntimeException     目标文件不能被创建时会抛出异常
     * @see #FileCallBack(File)
     */
    public FileCallBack(File target, boolean noExceptionThrow) {
        if (!noExceptionThrow) {
            checkFile(target);
        }
        this.noExceptionThrow = noExceptionThrow;
        this.mTarget = target;
    }

    /**
     * 检查文件是否合法
     *
     * @param file
     */
    private static void checkFile(File file) {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        if (file.exists() && !file.canWrite()) {
            throw new RuntimeException("file \"" + file.getPath() + "\" already exists but can't write");
        } else {
            checkParent(file.getParentFile());
        }
    }

    /**
     * 检查文件夹是否可写
     *
     * @param file
     */
    private static void checkParent(File file) {
        if (file == null) {
            throw new RuntimeException("file is not exists and parent dir / can't write");
        }
        if (file.canWrite()) {
            return;
        }
        if (file.exists()) {
            throw new RuntimeException("file is not exists and parent dir \"" + file.getPath() + "\" can't write");
        } else {
            checkParent(file.getParentFile());
        }

    }

    /**
     * 实现响应到 File 的转换
     *
     * @param response OkHttp Response 服务器响应
     * @return Result 转换后的结果
     * @throws Exception
     */
    @Override
    protected final File convert(Response response) throws Exception {
        File target = mTarget;
        if (noExceptionThrow) {
            checkFile(mTarget);
        }
        if (mTarget.isDirectory()) {
            target = new File(mTarget, getFileName(response));
        }
        File parentFile = mTarget.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        InputStream inputStream = response.body().byteStream();
        OutputStream outputStream = new FileOutputStream(target);
        byte[] buff = new byte[4096];
        int len;
        while ((len = inputStream.read(buff)) != -1) {
            outputStream.write(buff, 0, len);
        }
        inputStream.close();
        outputStream.close();
        return mTarget;
    }

    /**
     * 获取文件名
     *
     * @param response
     * @return
     */
    private String getFileName(Response response) {
        String fileName = null;
        String desp = response.header("Content-Disposition", null);
        if (desp != null) {
            desp = desp.replaceFirst("attachment;\\ ?filename\\ ?=\\ ?", "");
            if (desp.startsWith("\"") && desp.endsWith("\"") && desp.length() > 1) {
                desp = desp.substring(1, desp.length() - 1);
            }
            fileName = desp;
        }
        if (fileName == null) {
            fileName = System.currentTimeMillis() + ".tmp";
        }
        return fileName;
    }
}
