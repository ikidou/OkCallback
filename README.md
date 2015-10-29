# OkCallBack

OkHttp Async Callback for Android and java [ ![Download](https://api.bintray.com/packages/ikidou/maven/okcallback/images/download.svg) ](https://bintray.com/ikidou/maven/okcallback/_latestVersion)

支持Android环境和Java环境，如果在Android中使用，结果在主线程中回调，而不用作任何配置修改。

该库支持Gson，通过泛型可直接指定结果类型，并自转换，无需手动转换。

同时，如提供的类不能满足你的需求，还可直接继承 OkCallBack 以获得更多的支持，
但结果的回调依旧会在主线程中执行，可直接操作UI，不用必再使用Handler.post()和Handler.sendMessage()。


## Classes
- OkCallBack
	- ByteArrayCallBack
	- FileCallBack
	- GsonCallBack
	- JSONArrayCallBack
	- JSONObjectCallBack
	- StringCallBack
- Dispatcher
    - DefaultAndroidDispatcher
    - DefaultJavaDispatcher

## Examples 

### Use before

```java
public void useBefore(View v) {
    Request request = new Request.Builder().get().url(url).build();
    okHttpClient.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // TODO update UI
                }
            });
        }
        @Override
        public void onResponse(Response response) throws IOException {
            Gson gson = new Gson();
            Type type = new TypeToken<Resp<List<User>>>() {
            }.getType();
            final Resp<List<User>> resp = gson.fromJson(response.body().charStream(), type);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // TODO update UI
                }
            });
            
        }
    });
}

```

### Use after

```java
private OkHttpClient okHttpClient = new OkHttpClient();
/**
 * Google Gson example 
 */
public void gson(View v) {
    String url = URL_BASE + "resp.json";
    Request request = new Request.Builder().get().url(url).build();
    okHttpClient.newCall(request).enqueue(new GsonCallBack<Resp<List<User>>>() {
        /** UI Thread for android ,calling thread for java */
        @Override
        public void onSuccess(<Resp<List<User>>> result) {
            // TODO update UI
        }
        /** UI Thread*/
        @Override
        public void onError(int code, Request request, Exception e) {
            // TODO update UI
        }
    });
}

```


更多示例 [MainActivity](sample/src/main/java/cn/ikidou/sample/okcallback/MainActivity.java)

## Download
```gradle
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'cn.ikidou:okcallback:1.0.0'
}
allprojects {
    repositories {
        jcenter()
        maven{
            url 'https://dl.bintray.com/ikidou/maven'
        }
    }
}
```
[Download Jar](https://dl.bintray.com/ikidou/maven/cn/ikidou/okcallback/1.0.0/)

License
-------
    Copyright 2015 ikidou
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.