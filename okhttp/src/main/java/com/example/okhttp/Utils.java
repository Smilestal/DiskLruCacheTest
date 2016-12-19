package com.example.okhttp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.Buffer;

/**
 * Created by POST on 2016/12/17.
 */
public class Utils {

    public static void httpGet() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("https://github.com/hongyangAndroid")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }

        });
    }

    public static void httpPost() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("key", "value")
                .build();

        final Request request = new Request.Builder()
                .url("https://github.com/hongyangAndroid")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    /**
     * 查看参数
     *
     * @param requestBody
     * @return
     */
    public String getParameters(RequestBody requestBody) {
        try {
            final Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            //
            BufferedInputStream bufferedInputStream = new BufferedInputStream(buffer.inputStream());
            final StringBuffer resultBuffer = new StringBuffer();
            byte[] inputBytes = new byte[1024];
            while (true) {
                int count = bufferedInputStream.read(inputBytes);
                if (count <= 0) {
                    break;
                }
                resultBuffer.append(new String(Arrays.copyOf(inputBytes, count), Util.UTF_8));
            }
            final String parameter = URLDecoder.decode(resultBuffer.toString(),
                    Util.UTF_8.name());
            bufferedInputStream.close();


            return parameter;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
