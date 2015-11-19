package cn.ikidou.sample.okcallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.ikidou.okcallback.ByteArrayCallBack;
import cn.ikidou.okcallback.FileCallBack;
import cn.ikidou.okcallback.GsonCallBack;
import cn.ikidou.okcallback.JSONArrayCallBack;
import cn.ikidou.okcallback.JSONObjectCallBack;
import cn.ikidou.okcallback.OkCallBack;
import cn.ikidou.okcallback.StringCallBack;
import cn.ikidou.okcallback.sample.R;


public class MainActivity extends Activity {
    private static final String URL_BASE = "https://raw.githubusercontent.com/ikidou/OkCallback/master/sample/data/";

    private OkHttpClient okHttpClient = new OkHttpClient();
    private TextView contentTextView;
    private StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentTextView = (TextView) findViewById(R.id.tv_content);
    }

    public void byteArray(View v) {
        String url = URL_BASE + "string.txt";
        getCall(url).enqueue(new ByteArrayCallBack() {
            @Override
            public void onSuccess(Headers headers, byte[] result) {
                setText("byteArray:" + new String(result));
            }

            @Override
            public void onError(int code, Request request, Exception e) {
                setText("ByteArray Error:" + e.getMessage());
            }
        });
    }

    public void file(View v) {
        String url = URL_BASE + "string.txt";
        File file = new File(getCacheDir(), "fileTest.txt");
        getCall(url).enqueue(new FileCallBack(file) {
            @Override
            public void onSuccess(Headers headers, File result) {
                setText("FileCallBack: \n\tfile size:" + result.length() + " \n\tfile name:" + result.getName());
                result.delete();
            }

            @Override
            public void onError(int code, Request request, Exception e) {
                setText("FileCallBack Error:" + e.getMessage());
            }
        });
    }

    public void gson(View v) {
        String url = URL_BASE + "resp.json";
        getCall(url).enqueue(new GsonCallBack<Resp<List<User>>>() {
            @Override
            public void onSuccess(Headers headers, Resp<List<User>> result) {
                setText("GsonCallBack:\n\t" + result);
            }

            @Override
            public void onError(int code, Request request, Exception e) {
                setText("GsonCallBack Error:" + e.getMessage());
            }
        });
    }

    public void jsonArray(View v) {
        String url = URL_BASE + "arr.json";
        getCall(url).enqueue(new JSONArrayCallBack() {
            @Override
            public void onSuccess(Headers headers, JSONArray result) {
                setText("JSONArrayCallBack:\n\t" + result);
            }

            @Override
            public void onError(int code, Request request, Exception e) {
                setText("JSONArrayCallBack Error:\n\t" + e.getMessage());
            }
        });

    }

    public void jsonObj(View v) {
        String url = URL_BASE + "obj.json";
        getCall(url).enqueue(new JSONObjectCallBack() {
            @Override
            public void onSuccess(Headers headers, JSONObject result) {
                setText("JSONObjectCallBack:\n\t" + result);
            }

            @Override
            public void onError(int code, Request request, Exception e) {
                setText("JSONObjectCallBack Error:\n\t" + e.getMessage());
            }
        });
    }

    public void string(View v) {
        String url = URL_BASE + "string.txt";
        getCall(url).enqueue(new StringCallBack() {
            @Override
            public void onSuccess(Headers headers, String result) {
                setText("StringCallBack:\n\t" + result);
            }

            @Override
            public void onError(int code, Request request, Exception e) {
                setText("StringCallBack Error:\n\t" + e.getMessage());
            }
        });
    }

    public void advance(View v) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("loading...");
        dialog.setCancelable(false);
        String url = URL_BASE + "string.txt1"; // 404
        getCall(url).enqueue(new OkCallBack<String>() {
            @Override
            protected void onStart() {
                dialog.show();
            }

            @Override
            protected String convert(Response response) throws IOException {
                String s = response.body().string();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return s;
            }

            @Override
            public void onSuccess(Headers headers, String result) {
                setText("advance OkCallBack<String>:\n\t" + result);
            }

            @Override
            public void onError(int code, Request request, Exception e) {
                setText("advance OkCallBack<String> Error:\n\t" + e.getMessage());
            }

            @Override
            protected void onComplete(boolean successful) {
                // 404 ,checkCode() return false,so successful is true(No Exception caught)
                dialog.dismiss();
                if (successful) {
                    Toast.makeText(MainActivity.this, "Get Data Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Get Data failed", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * 是否检查Http code
             * @return when code >= 400 never call onError if return false
             */
            @Override
            protected boolean checkCode() {
                return false;
            }
        });
    }

    public void clear(View v) {
        sb.delete(0, sb.length());
        contentTextView.setText(sb);
    }

    public void setText(String text) {
        sb.append(text);
        sb.append("\n\n");
        contentTextView.setText(sb);
    }

    public Call getCall(String url) {
        Request request = new Request.Builder().get().url(url).build();
        return okHttpClient.newCall(request);
    }
}
