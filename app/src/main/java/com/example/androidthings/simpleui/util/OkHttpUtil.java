package com.example.androidthings.simpleui.util;

import android.util.Log;

import com.example.androidthings.simpleui.base.BaseApplication;
import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.CacheManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostBytesBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.ResultCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttpUtil {
    private static final String TAG = "OkHttpUtil";
    private static final String TAG_PAGEINDEX = "pageIndex";
    private static final int INTEGER_1000 = 1000;
    /** 防止重复提交 */
    private static final Map<String, Long> URL_REQUEST = new HashMap<String, Long>();

    /**
     * 无参get请求
     *
     * @param url      url
     * @param callback 回调中new ResultCallback<T>()必须指明具体类型
     */
    public static void get(String url, ResultCallback callback) {
        get(null, url, null, callback);
    }

    /**
     * 有参get请求
     *
     * @param url      url
     * @param params   参数
     * @param callback 回调中new ResultCallback<T>()必须指明具体类型
     */
    public static void get(String url, Map params, ResultCallback callback) {
        get(null, url, params, callback);
    }

    /**
     * @param tag      请求标记（建议使用页面及TAG）
     * @param url      url
     * @param callback 回调中new ResultCallback<T>()必须指明具体类型
     */
    public static void get(String tag, String url, ResultCallback callback) {
        get(tag, url, null, callback);
    }

    /**
     * 参数为map的get请求
     *
     * @param tag      请求标记（建议使用页面及TAG）
     * @param url      url
     * @param params   参数
     * @param callback 回调中new ResultCallback<T>()必须指明具体类型
     * @param- cacheAble 是否需要缓存数据，如果需要缓存，下次优先取缓存的数据
     * @param- cacheKey 缓存数据的key
     * @param- cacheTime 缓存时间
     * @param- afterCode 返回码  900-自动刷新
     * @param- cacheUpdate 是否强制更新
     */
    public static void get(Object tag, String url, Map params, ResultCallback callback) {
        get(tag, -1, url, params, callback);
    }


    /**
     * 带有id的get请求
     *
     * @param tag      标记
     * @param id       id值
     * @param url      请求url
     * @param params   参数1
     * @param callback 回调
     */
    public static void get(Object tag, int id, String url, Map params, ResultCallback callback) {
        if (params != null && params.containsKey(TAG_PAGEINDEX)) {
            WebFrontUtil.handlePage(params);
        }
        String cacheAble = StringUtil.getMapString(params, "cacheAble");
        String cacheKey = StringUtil.getMapString(params, "cacheKey");
        String cacheTime = StringUtil.getMapString(params, "cacheTime");
        String cacheUpdate = StringUtil.getMapString(params, "cacheUpdate");
        //是否需要刷新缓存，需要强制刷新时，为true
        cacheUpdate = StringUtil.isNotEmpty(cacheUpdate) ? cacheUpdate : "false";
        String afterCode = StringUtil.getMapString(params, "afterCode");
        Long cacheTimeMill = StringUtil.isNotEmpty(cacheTime) ? Long.valueOf(cacheTime) : 0L;
        boolean existed = ("true".equals(cacheAble) && ("false".equals(cacheUpdate)||!NetUtil.isNetworkConnected(BaseApplication.context()))
                && CacheManager.isCacheDataValid(BaseApplication.context(), cacheKey, cacheTimeMill));
        if (existed) {
            Log.i("OkHttpRequest", "cache get:------" + cacheKey);
            new CacheManager().new ReadCacheTask(BaseApplication.context(), callback, afterCode,id).execute(cacheKey);
        } else {
            GetBuilder builder = OkHttpUtils.get().url(url);
            if (tag != null) {
                builder.tag(tag);
            }
            if (id != -1) {
                builder.id(id);
            }
            if (params != null) {
                String jsonParam = GsonUtil.toJSONStr(params);
                Map p = new HashMap(4);
                p.put("queryJson", jsonParam);
                Log.i(TAG, "url-----" + url + "params--:" + p.toString());
                builder.params(p);
            }
            builder.build().execute(convertCallback(callback), BaseApplication.context(), Boolean.valueOf(cacheAble),
                    Boolean.valueOf(cacheUpdate), cacheKey, Long.valueOf(cacheTimeMill));
        }

    }


    /**
     * put请求(一般用于更新)
     *
     * @param url      url
     * @param params   参数
     * @param callback 回调中new ResultCallback<T>()必须指明具体类型
     */
    public static void put(String url, Map params, ResultCallback callback) {
        put(null, url, params, callback, true);
    }


    /**
     * put请求(一般用于更新)
     *
     * @param tag      请求标记（建议使用页面及TAG）
     * @param url      url
     * @param params   参数
     * @param callback 回调中new ResultCallback<T>()必须指明具体类型
     */
    public static void put(Object tag, String url, Map params, ResultCallback callback) {
        put(tag, url, params, callback, true);
    }

    /**
     * put请求(一般用于更新)
     *
     * @param tag      请求标记（建议使用页面及TAG）
     * @param url      url
     * @param params   参数
     * @param callback 回调中new ResultCallback<T>()必须指明具体类型
     */
    public static void put(Object tag, String url, Map params, ResultCallback callback, boolean isUrlRecord) {
        if (!isUrlRecord || isValid(url, params)) {
            Log.i("put url---:", url);
            Log.i("put param---:", params.toString());
            Log.i(TAG, "url-----" + url + "params--:" + params.toString());
            callback = callback != null ? callback : DEFAULT_RESULT_CALLBACK;
            String jsonParam = GsonUtil.toJSONStr(params);
            OkHttpUtils.put().url(url).tag(tag).requestBody(jsonParam).build().execute(convertCallback(callback));
        } else {
            NetUtil.closeDialog();
        }
    }


    /**
     * post请求(一般用于创建create)
     *
     * @param url      url
     * @param params   参数
     * @param callback 不关心返回值的时,该参数传入null即可
     */
    public static void post(String url, Map params, ResultCallback callback) {
        post(null, url, params, callback, true);
    }

    /**
     * post请求(一般用于创建create)
     *
     * @param tag      请求标记（建议使用页面及TAG）
     * @param url      url
     * @param params   参数
     * @param callback 不关心返回值的时,该参数传入null即可
     */
    public static void post(Object tag, String url, Map params, ResultCallback callback) {
        post(tag, url, params, callback, true);
    }

    /**
     * post请求(一般用于创建create)
     *
     * @param tag      请求标记（建议使用页面及TAG）
     * @param url      url
     * @param params   参数
     * @param callback 回调中new ResultCallback<T>()必须指明具体类型
     */
    public static void post(Object tag, String url, Map params, ResultCallback callback, boolean isUrlRecord) {
        if (!isUrlRecord || isValid(url, params)) {
            Log.i("HTTP---url == :", url);
            callback = callback != null ? callback : DEFAULT_RESULT_CALLBACK;
            String jsonParam = GsonUtil.toJSONStr(params);
            Log.i("HTTP---param == ",jsonParam);
            OkHttpUtils.postString().url(url).tag(tag).mediaType(MediaType.parse("application/json;charset=utf-8"))
                    .content(jsonParam).build().execute(convertCallback(callback));
        } else {
            NetUtil.closeDialog();
        }
    }

    /**
     * 判断url是否有效，防止重复提交
     * 如果同一个连接一秒内访问多次，则被取消
     *
     * @param url
     * @return
     */
    private static boolean isValid(String url, Map params) {
        Long currentTimeMillis = System.currentTimeMillis();
        if (!URL_REQUEST.containsKey(url + params)) {
            URL_REQUEST.put(url + params, System.currentTimeMillis());
            return true;
        }
        if (currentTimeMillis - URL_REQUEST.get(url + params) > INTEGER_1000) {
            URL_REQUEST.remove(url + params);
            return true;
        }
        return false;
    }

    /**
     * 上传图片
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void postContent(String url, Map params, ResultCallback callback) {
        postContent(url, params, null, callback);
    }

    /**
     * 上传图片
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void postContent(String url, Map params, String tag, ResultCallback callback) {
        callback = callback != null ? callback : DEFAULT_RESULT_CALLBACK;
        String jsonParam = new GsonBuilder().create().toJson(params);
        PostBytesBuilder builder = OkHttpUtils.postBytes().url(url).content(jsonParam.getBytes());
        if (StringUtil.isNotEmpty(tag)) {
            builder.tag(tag);
        }
        /// builder.build().writeTimeOut(300 * 1000L).execute(convertCallback(callback));
        builder.build().writeTimeOut(300 * 1000L).connTimeOut(30*1000L).readTimeOut(30*1000L).execute(convertCallback(callback));
        ///new OkHttpRequest.Builder().url(url).tag(tag).params(params).bytes(jsonParam.getBytes()).post(callback);
    }

    /**
     * 单个文件下载
     *
     * @param url          url
     * @param destFileDir  存储路径
     * @param destFileName 本地存储的文件名称
     * @param callback     回调中new ResultCallback<T>()必须指明具体类型
     */
    public static void downloadFile(String url, String destFileDir, String destFileName, ResultCallback callback) {
        downloadFile(null, url, destFileDir, destFileName, callback);
    }

    /**
     * @param url          url
     * @param destFileDir  存储路径
     * @param destFileName 本地存储的文件名称
     * @param tag          请求标记（建议使用页面及TAG）
     * @param callback     回调中new ResultCallback<T>()必须指明具体类型
     */
    public static void downloadFile(Object tag, String url, String destFileDir, String destFileName, final
    ResultCallback callback) {
        OkHttpUtils.get().url(url).tag(tag).build().execute(new FileCallBack(destFileDir, destFileName) {
            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                callback.inProgress(progress, id);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call.request(), e, id);
            }

            @Override
            public void onResponse(File response, int id) {
                callback.onResponse(response, id);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                callback.onAfter("", id);
            }
        });
    }

    private static Callback convertCallback(final ResultCallback callback) {
        if (callback == null) {
            return null;
        }
        return new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call.request(), e, id);
            }

            @Override
            public void onResponse(Object response, int id) {
                callback.onResponse(response, id);
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                callback.inProgress(progress, id);
            }

            @Override
            public void onAfter(int id) {
                callback.onAfter("", id);
            }

            @Override
            public void onBefore(Request request, int id) {
                callback.onBefore(request, id);
            }

            @Override
            public void cancel(Call call, Exception e, int id) {
                callback.cancel(call.request(), e, id);
            }
        };
    }

    /**
     * 根据tag取消请求
     *
     * @param tag
     */
    public static void cancelTag(Object tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }


    public static final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {

        }
    };
}
