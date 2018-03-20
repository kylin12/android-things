package com.example.androidthings.simpleui.util;

import android.content.Context;
import android.util.Log;

import com.example.androidthings.simpleui.entity.BaseResponseObject;

import java.util.HashMap;
import java.util.Map;

public class WebFrontUtil {

    private static final String VISIT_SITEID= "visitSiteId";
    /**
     * 获取基本参数
     *
     * @param paramMap paramMap
     * @return Map
     */
    public static Map<String, Object> getBaseParam(Context context, Map<String, Object> paramMap) {
        if (paramMap == null) {
            paramMap = new HashMap<>(4);
        }
        return paramMap;
    }

    public static HashMap<String, Object> getBaseParam() {
        HashMap<String, Object> paramMap = new HashMap<>(4);
        return paramMap;
    }

    /**
     * 分页参数转换 将pageIndex、pageIndex 转换成 firstResult、maxResult
     *
     * @param paramMap ()
     */
    public static void handlePage(Map<String, Object> paramMap) {
        int pageIndex;
        int pageSize;
        String pageIndexString = paramMap.get("pageIndex") != null ? paramMap.get("pageIndex").toString() : "1";
        String pageSizeString = paramMap.get("pageSize") != null ? paramMap.get("pageSize").toString() : "10";
        if (pageIndexString.length() > 0) {
            pageIndex = Integer.valueOf(pageIndexString);
        } else {
            pageIndex = 1;
        }
        if (pageSizeString.length() > 0) {
            pageSize = Integer.valueOf(pageSizeString);
        } else {
            pageSize = 10;
        }
        int firstResult = (pageIndex - 1) * pageSize;
        int maxResult = pageSize;
        paramMap.put("firstResult", firstResult);
        paramMap.put("maxResult", maxResult);
    }

    /**
     * 获取响应基本对象
     *
     * @param jsonData
     * @return
     */
    public static BaseResponseObject getResponseObject(String jsonData) {
        BaseResponseObject baseResponseObject = new BaseResponseObject(false, "", "");
        try {
            if (StringUtil.isNotEmpty(jsonData)) {
                baseResponseObject = (BaseResponseObject) GsonUtil.toBaseResponseObject(jsonData);
            }
        } catch (Exception ex) {
            Log.e("---->execute exception:", ex.getMessage());
            ex.printStackTrace();
        }
        return baseResponseObject;
    }


}
