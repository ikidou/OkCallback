# OkCallBack

OkHttp Async Callback for Android and java 

[ ![Download](https://api.bintray.com/packages/ikidou/maven/okcallback/images/download.svg) ](https://bintray.com/ikidou/maven/okcallback/_latestVersion)

同时支持Android环境和Java环境，如果在Android中使用，结果在主线程中回调，而不用作任何配置修改。

该库支持Gson，通过泛型可直接指定结果类型，并自转换，无需手动转换。

同时，如现有类不能满足你的需求，还可直接继承 `OkCallBack` 并实现 `convert(Response response)`方法 以获得更多的支持，例如使用Jackson或fastjson来解析数据。但结果的回调依旧会在主线程中执行，可直接操作UI，不用必再使用Handler.post()和Handler.sendMessage()。


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

## Usage
根据需要使用`OkCallBack` `GsonCallBack` `FileCallBack` `JSONArrayCallBack` `JSONObjectCallBack` `StringCallBack` `ByteArrayCallBack` 替换 `com.squareup.okhttp.Callback` 即可。

## Examples 

```java
private OkHttpClient okHttpClient = new OkHttpClient();
/**
 * Google Gson example 
 */
public void gson(View v) {
    String url = URL_BASE + "resp.json";
    Request request = new Request.Builder().get().url(url).build();
    okHttpClient.newCall(request).enqueue(new GsonCallBack<Resp<List<User>>>() {
        /** UI Thread for android ,request thread for java */
        @Override
        public void onSuccess(Headers headers, <Resp<List<User>>> result) {
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
### Gradle:
```gradle
compile 'cn.ikidou:okcallback:1.0.2'
```
### Maven:
```xml
<dependency>
	<groupId>cn.ikidou</groupId>
	<artifactId>okcallback</artifactId>
	<version>1.0.2</version>
</dependency>
```
[Download Jar](https://dl.bintray.com/ikidou/maven/cn/ikidou/okcallback/1.0.2/)
## Change Log
**v1.0.2**

1. FileCallBack 增加`onProgressChanged(long current, long total, float progress)` 方法，该方法会在proogress的变化较上次超过0.01%或时间较上次超过1s时回调，但时间并非使用定时器实现，只适用于文件较大时导致的回调变慢
1. FileCallBack 变更`FileCallBack(File, boolean)`构造方法，异常信息通过`onError`方法通知，第二个参数变为是否追加。
1. FileCallBack 增加`FileCallBack(File target, boolan append, int bufferSize, float limitProgress, int timeDiff)`构造方法
1. 修复FileCallBack 当target是一个文件夹时结果不正确的Bug


**v1.0.1** 

1. 添加`onStart`方法，并使其在构造方法中通过`Dispatcher`分发，保证最先执行
1. 使用`onComplete(boolean successful)`代替`afterAll(boolean successful)`
1. `afterAll(boolean successful)` 过时，但仍可使用


**v1.0.0** 初始化基础库


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