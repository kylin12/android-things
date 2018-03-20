package com.example.androidthings.simpleui.util;

import com.example.androidthings.simpleui.entity.BaseResponseObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GsonUtil {
    private static GsonBuilder gb = new GsonBuilder();

    /**
     * 函数名称: gsonToMap 函数描述: 将json字符串转换为map
     *
     * @param data
     * @return
     */
    public static Map<String, Object> parseGsonToMap(String data) {
        Gson g = gb.create();
        Map<String, Object> map = g.fromJson(data, new TypeToken<Map<String, String>>() {
        }.getType());
        return map;
    }

    /**
     * 转换为json格式的字符串
     *
     * @param obj
     * @return
     */
    public static String toJSONStr(Object obj) {
        Gson g = gb.create();
        return g.toJson(obj);
    }

    public static Gson getGson(){
        return gb.create();
    }

    /**
     * json字符串转换指定对象
     *
     * @param jsonString
     * @param pojoCalss
     * @return
     */
    public static <T> T getObject(String jsonString, TypeToken<T> pojoCalss) {
        try {
            Gson g = gb.create();
            return g.fromJson(jsonString, pojoCalss.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取JsonObject
     *
     * @param json
     * @return
     */
    public static JsonObject parseJson(String json) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        return jsonObj;
    }

    /**
     * 根据json字符串返回BaseResponseObject对象
     *
     * @param json
     * @return
     */
    public static BaseResponseObject toBaseResponseObject(String json) {
        BaseResponseObject respObj = new BaseResponseObject(false, "", "");
        Map map = toMap(parseJson(json));
        if (map != null) {
            String respStatus = map.get("responseStatus") != null ? map.get("responseStatus").toString() : "false";
            respObj.setResponseStatus(Boolean.valueOf(respStatus));
            String respCode = map.get("responseCode") != null ? map.get("responseCode").toString() : "";
            respObj.setResponseCode(respCode.replace("\"", ""));
            String message = map.get("responseMessage") != null ? map.get("responseMessage").toString() : "";
            respObj.setResponseMessage(message);
            Object data = map.get("responseData");
            HashMap respData = (data != null && !"null".equals(data.toString())) ? (HashMap) data : new HashMap(4);
            respObj.setResponseData(respData);
            String respPK = map.get("responsePk") != null ? map.get("responsePk").toString() : "0";
            respObj.setResponsePk(Long.valueOf(respPK));
        }
        return respObj;
    }

    /**
     * 根据json字符串返回Map对象
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(String json) {
        return toMap(parseJson(json));
    }

    private static Pattern NUMBER_PATTERN = Pattern.compile("\"(\\d)*\"");
    /**
     * 将JSONObjec对象转换成Map-List集合
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(JsonObject json) {
        Map<String, Object> map = new HashMap<String, Object>(4);
        Set<Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ) {
            Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();

            boolean existed = value != null && ("null".equals(value.toString()) || "{}".equals(value.toString().trim()));

            if (value instanceof JsonArray){
                map.put((String) key, toList((JsonArray) value));
            }else if (value instanceof JsonObject){
                map.put((String) key, toMap((JsonObject) value));
            }
            else if (existed) {
                map.put((String) key, null);
            } else {
                if (value != null) {
                    String valStr = value.toString();
                    if (valStr.length() > 2 && valStr.startsWith("\"") && valStr.endsWith("\"")) {
                        valStr = valStr.substring(1, valStr.length() - 1);
                    }

                    Matcher m = NUMBER_PATTERN.matcher(valStr.toString());
                    while (m.find()) {
                        String val = m.group(0);
                        valStr = valStr.replaceFirst("\"(\\d)*\"", valStr.substring(1, val.length() - 1));
                    }
                    valStr = StringUtil.replaceEscape(valStr, "\b\r\n\t<br>\\");
                    value = valStr;
                } else {
                    value = "";
                }
                map.put((String) key, value.toString());
            }
        }
        return map;
    }

    /**
     * 将JSONArray对象转换成List集合
     *
     * @param json
     * @return
     */
    public static List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < json.size(); i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                list.add(toMap((JsonObject) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }

    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        if(StringUtil.isNotEmpty(json)){
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }

    /**
     * 将Map转化为Json
     *
     * @param map
     * @return String
     */
    public static String mapToJson(List<HashMap> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }
}
