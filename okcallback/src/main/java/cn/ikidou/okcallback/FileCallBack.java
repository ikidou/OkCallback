package cn.ikidou.okcallback;

import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import cn.ikidou.okcallback.dispatcher.Dispatcher;

/**
 * 该类继承自 OkCallBack<File>，实现自动将结果写入文件
 *
 * @author 怪盗kidou bestkidou@gmail.com
 * @see OkCallBack
 * @see GsonCallBack
 * @see JSONArrayCallBack
 * @see JSONObjectCallBack
 * @see StringCallBack
 * @since 1.0.0
 */
public abstract class FileCallBack extends OkCallBack<File> {
    private final File mTarget;
    public final int BUFFER_SIZE;
    public final long TIME_DIFF;
    public final float LIMIT_PROGRESS;
    private boolean append;
    private float oldProgress;
    private long lastTime;

    /**
     * 构造方法
     *
     * @param target 目标文件(夹),如果<code>target<code/>是文件夹，则创建临时文件
     */
    public FileCallBack(File target) {
        this(target, false, 4096, 0.01f, 1000);
    }

    /**
     * 构造方法
     *
     * @param target 目标文件(夹),如果<code>target<code/>是文件夹，则创建临时文件
     * @param append 是否为追加模式
     */
    public FileCallBack(File target, boolean append) {
        this(target, append, 4096, 0.01f, 1000);
    }

    /**
     * 可以通过该方法可实现对 {@link #onProgressChanged(long, long, float)} 回调快慢进行控制以及缓冲区大小的控制
     *
     * @param target        目标文件(夹),如果<code>target<code/>是文件夹，则创建临时文件
     * @param append        是否为追加模式
     * @param bufferSize    下载文件时，创建的缓冲区大小，单位字节.
     * @param limitProgress 回调 {@link #onProgressChanged(long, long, float)} 时，进度最小差异，百分比，默认0.01f
     * @param timeDiff      回调 {@link #onProgressChanged(long, long, float)} 时，时间最大差异，默认1000毫秒
     */
    public FileCallBack(File target, boolean append, int bufferSize, float limitProgress, int timeDiff) {
        this.mTarget = target;
        if (bufferSize < 1) {
            bufferSize = 4096;
        }
        if (limitProgress < 0.01f) {
            limitProgress = 0.01f;
        }
        if (timeDiff < 1) {
            timeDiff = 1000;
        }
        this.append = append;
        this.BUFFER_SIZE = bufferSize;
        this.LIMIT_PROGRESS = limitProgress;
        this.TIME_DIFF = timeDiff;
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
        if (file.exists()) {
            if (!file.canWrite()) {
                throw new RuntimeException("file \"" + file.getPath() + "\" already exists but can't write");
            }
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
        if (file.exists()) { // file exists and can't write
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

        checkFile(mTarget);

        if (mTarget.isDirectory()) {
            target = new File(mTarget, getFileName(response));
        }

        File parentFile = target.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        long total = response.body().contentLength();
        long current = 0;
        updateProgress(current, total);

        InputStream inputStream = response.body().byteStream();
        OutputStream outputStream = new FileOutputStream(target, append);

        byte[] buff = new byte[BUFFER_SIZE];
        int len;
        while ((len = inputStream.read(buff)) != -1) {
            outputStream.write(buff, 0, len);
            current += len;
            updateProgress(current, total);
        }
        inputStream.close();
        outputStream.close();

        return target;
    }

    /**
     * 获取文件名
     */
    private String getFileName(Response response) {
        String fileName = null;
        String fileNameFromHeader = response.header("Content-Disposition", null);

        if (fileNameFromHeader != null) {
            fileNameFromHeader = fileNameFromHeader.replaceFirst("attachment;\\ ?filename\\ ?=\\ ?", "");
            if (fileNameFromHeader.startsWith("\"") && fileNameFromHeader.endsWith("\"") && fileNameFromHeader.length() > 1) {
                fileNameFromHeader = fileNameFromHeader.substring(1, fileNameFromHeader.length() - 1);
            }
            fileName = fileNameFromHeader;
        }

        String url = response.request().urlString();
        int index = url.indexOf("?"); // 参数
        if (index >= 0) {
            url = url.substring(0, index); // 去除参数
        }

        index = url.lastIndexOf("/");
        if (index > 0 && (index + 1) <= url.length()) {
            url = url.substring(index + 1);
        }
        if (url.length() > 0 || url.matches("\\w+\\.\\w{3,}")) {
            fileName = url;
        }

        if (fileName == null) {
            fileName = "FileCallBack_" + System.currentTimeMillis() + ".tmp";
        }

        return fileName;
    }


    private void updateProgress(final long current, final long total) {
        final float progress;

        if (total < 0) {
            progress = 0f;
        } else {
            progress = (current * 1.0f / total) * 100;
        }

        long currentTime = System.currentTimeMillis();

        if (current == total || progress - oldProgress > LIMIT_PROGRESS || currentTime - lastTime > TIME_DIFF) {
            lastTime = currentTime;
            oldProgress = progress;
            Dispatcher.getDefault().dispatch(new Runnable() {
                @Override
                public void run() {
                    onProgressChanged(current, total, progress);
                }
            });
        }
    }

    /**
     * 当下载进度发生当化时的回调方法，当进度大于0.01%或时间大于1s时回调（大文件适用，非定时）
     *
     * @param current  当前已读取的字节数
     * @param total    总字节数，如果不确定时为-1
     * @param progress 已百分比化的下载进度，如： 80.63
     * @since 1.0.2
     */
    protected void onProgressChanged(long current, long total, float progress) {
    }
}
