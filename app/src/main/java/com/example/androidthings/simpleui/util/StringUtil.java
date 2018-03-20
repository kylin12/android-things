package com.example.androidthings.simpleui.util;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {

    private static final int INTEGER_2 = 2;
    private static final int INTEGER_5 = 5;
    private static final int INTEGER_20 = 20;
    private static final int INTEGER_110 = 110;
    private static final int INTEGER_1000 = 1000;
    private static final int INTEGER_10000 = 10000;
    private static final int INTEGER_100000 = 100000;

    private static final String MEDPLUS = "medplus";
    private static final String YIDING = "yiding";
    private static final String SCENE = "scene";

    private static final char CHAR_SPACE = ' ';

    /**
     * 缺省的字符串分割符
     */
    public static String DEFAULT_DELIM = "$*";

    public static String getMapString(Map paramMap, String key) {
        String result = "";
        if (paramMap != null && isNotEmpty(key)) {
            result = paramMap.get(key) != null ? paramMap.get(key).toString() : "";
            result = replaceEscape(result, "\n\r");
        }
        return result;
    }

    public static String replaceEscape(String str, String escapeReg) {
        if (str != null) {
            String str1 = "\n";
            if (escapeReg.contains(str1)) {
                str = str.replace("\\n", "\n");
            }
            String str2 = "\r";
            if (escapeReg.contains(str2)) {
                str = str.replace("\\r", "\r");
            }
            String str3 = "<br>";
            if (escapeReg.contains(str3)) {
                str = str.replace("<br>", "");
            }
            String str4 = "\b";
            if (escapeReg.contains(str4)) {
                str = str.replace("\\b", "\b");
            }
            String str5 = "\t";
            if (escapeReg.contains(str5)) {
                str = str.replace("\\t", "\t");
            }
            String str6 = "\\";
            if (escapeReg.contains(str6)) {
                str = str.replace("\\", "");
            }
        }
        return str;
    }

    /**
     * 判断医师年龄   要求20-110之间
     *
     * @param age
     * @return
     */
    public static boolean isRightAge(String age) {
        if (isNotEmpty(age)) {
            int num = Integer.parseInt(age);
            if (num >= INTEGER_20 && num <= INTEGER_110) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * 获取bundle String类型参数
     *
     * @param bundle
     * @param key
     * @return
     */
    public static String getBindleString(Bundle bundle, String key) {
        String result = "";
        if (bundle != null && bundle.getString(key) != null) {
            result = bundle.getString(key).toString();
        }
        return result;
    }
    /**
     * 转换职称的格式  2_讲师，3_教授  --->  讲师、教授
     *
     * @param medicalTitle
     * @return
     */
    public static String formatStringHasUnderLine(String medicalTitle) {
        StringBuilder sb = new StringBuilder();
        if (isNotEmpty(medicalTitle)) {
            String[] titles = medicalTitle.split(",");
            for (int i = 0; i < titles.length; i++) {
                int index = titles[i].indexOf("_");
                if (index != -1) {
                    sb.append(titles[i].substring(index + 1)).append("、");
                } else {
                    sb.append(titles[i]).append("、");
                }
            }
        } else {
            return sb.substring(0);
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 转换职称的格式后只取第一个  2_讲师，3_教授  --->  讲师
     *
     * @param medicalTitle
     * @return
     */
    public static String formatStringHasUnderLineFirst(String medicalTitle) {
        StringBuilder sb = new StringBuilder();
        if (isNotEmpty(medicalTitle)) {
            String[] titles = medicalTitle.split(",");
            if (titles.length > 0) {
                String str = "_";
                if (titles[0].contains(str)) {
                    int index = titles[0].indexOf("_");
                    titles[0] = titles[0].substring(index + 1);
                    return titles[0];
                }
            }
        }
        return "";
    }

    /**
     * 截取指定的字符串长度（按字符算，汉字占两个字节，字母占一个字节）
     *
     * @param source 原字符串
     * @param length 规定的字节长度
     * @return 截取后的字符串
     */
    public static String getString(String source, int length) {
        //当前字符串长度
        int len = 0;
        int subLength = 1;
        try {
            for (int i = 0; i < source.length(); i++) {
                char c = source.charAt(i);
                int highByte = c >>> 8;
                len += highByte == 0 ? 1 : 2;
                if (len == length || len == (length + 1)) {
                    return source.substring(0, subLength) + "...";
                }
                subLength++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }
    /**
     * 证件号加密
     *
     * @param str
     * @return
     */
    public static String getPasswordString(String str) {
        if (TextUtils.isEmpty(str)){
            return "";
        }
        if (str.length() > INTEGER_5) {
            String firstName = str.substring(0, 4);
            String lastName = str.substring(str.length() - 1, str.length());
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < str.length() - INTEGER_5; i++) {
                strBuf.append("*");
            }
            str = firstName + strBuf + lastName;
            return str;
        } else {
            return str;
        }
    }

    /**
     * 截取指定的字符长度，按个数算
     * @source 原字符串
     * @param num 规定的字节长度
     * @return 截取后的字符串
     */
    public static String formatReplyTitle(String str, int num) {
        if (str != null && str.length() > num) {
            return str.substring(0, num) + "...";
        }
        return str;
    }

    /**
     * 资料八项资料  像地点 单位条目拼串
     *
     * @param str1
     * @param str2
     * @param str3
     * @return
     */
    public static String formatStrings(String str1, String str2, String str3) {
        StringBuilder newStr = new StringBuilder();
        if (isNotEmpty(str1)){
            newStr.append(str1);
        }
        if (isNotEmpty(str2)){
            newStr.append("  ").append(str2);
        }
        if (isNotEmpty(str3)){
            newStr.append("  ").append(str3);
        }
        return newStr.toString();
    }

    /**
     * 1、长度计算文本显示的行数
     * 2、根据屏幕的宽度手动设置文本内容换行
     * 3、记录每一行显示的字符个数
     *
     * @param tv
     * @return
     */
    public static Map autoSplitText(TextView tv, String fullText, String ellipsis, String space, String foldText, int
            maxLines) {
        //原始文本
        final String rawText = fullText;
        //paint，包含字体等信息
        final Paint tvPaint = tv.getPaint();
        //控件可用宽度
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight();
        if (tvWidth == 0) {
            return null;
        }
        //行字符数量集合
        List<Integer> list = new ArrayList();
        //行内字符数
        int maxLineLength = 0;
        //行数
        float lastPercent = 0f;
        //将原始文本按行拆分
        String rawTextLine = rawText.replaceAll("\r", "").replaceAll("\n", "").split("\n")[0];
        //手动换行后的文本内容
        StringBuilder sbNewText = new StringBuilder();
        //测量文本长度
        if (tvPaint.measureText(rawTextLine) <= tvWidth) {
            //如果整行宽度在控件可用宽度之内，就不处理了
            sbNewText.append(rawTextLine);
        } else {
            //尾部追加内容的宽度 36 44 44 20
            float operatorWidth = tvPaint.measureText(ellipsis) + tvPaint.measureText(space) + tvPaint.measureText
                    (foldText);
            //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
            //字符宽度
            float lineWidth = 0;
            float tvWidthCopy = tvWidth;
            //计算文本内容内每个字符所占用的宽度
            for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                char ch = rawTextLine.charAt(cnt);
                //计算当前字符的宽度
                lineWidth += tvPaint.measureText(String.valueOf(ch));
                //判断当前字符多占的宽度是否大于控件可用宽度
                if (lineWidth <= tvWidthCopy) {
                    sbNewText.append(ch);
                    //当前行字符数加1
                    maxLineLength++;
                } else {
                    //保存行的字符数
                    list.add(maxLineLength);
                    if (list.size() == maxLines - 1) {
                        //如果保存的行数等于最大行数减1，则下一行的控件可用宽度要减去尾部追加内容的宽度。
                        tvWidthCopy = tvWidthCopy - operatorWidth;
                    } else {
                        tvWidthCopy = tvWidth;
                    }
                    maxLineLength = 0;
                    //加入换行符
                    sbNewText.append("\n");
                    lineWidth = 0;
                    --cnt;
                }
            }
            //计算遗留字符所占的宽度
            if (maxLineLength != 0) {
                if (list.size() == maxLines && lineWidth < (operatorWidth)) {
                    //如果是最后一行，并且遗留的字符宽度小于尾部追加内容的宽度。遗留的字符将不计下一行
                    lastPercent = list.size() + maxLineLength / (float) list.get(0);
                } else {
                    //否则追加到行数集合中
                    list.add(maxLineLength);
                    lastPercent = list.size() - 1 + maxLineLength / (float) list.get(0);
                }
            }
        }
        Map map = new HashMap(4);
        //处理后的文本
        map.put("workingText", sbNewText.toString());
        //行字符数集合
        map.put("lengthLine", list);
        //行数
        map.put("lastPercent", lastPercent);
        return map;
    }

    /**
     * 将字符串使用缺省分割符（逗号）划分的单词数组。
     *
     * @param source 需要进行划分的原字符串
     * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组。
     */
    public static String[] split(String source) {
        return split(source, DEFAULT_DELIM);
    }

    /**
     * 此方法将给出的字符串source使用delim划分为单词数组。 注意：分隔字符串中每一个 <b>(ANY) </b>的字符都作为独立的分割符。 <br>
     * 举个例子： <br>
     * "mofit.com.cn"用"com"分割后的结果是三个字符串"fit."、"."和"n"，而不是"mofit."和".cn"。
     *
     * @param source 需要进行划分的原字符串
     * @param delim  单词的分隔字符串
     * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组，
     * 如果delim为null则使用逗号作为分隔字符串。
     */
    public static String[] split(String source, String delim) {
        String[] wordLists;
        if (source == null) {
            wordLists = new String[1];
            wordLists[0] = source;
            return wordLists;
        }
        if (delim == null) {
            delim = DEFAULT_DELIM;
        }
        StringTokenizer st = new StringTokenizer(source, delim);

        int total = st.countTokens();
        wordLists = new String[total];
        for (int i = 0; i < total; i++) {
            wordLists[i] = st.nextToken();
        }
        return wordLists;
    }
    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0) && isBlank(str);
    }
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }
    /**
     * 判断是否为空
     *
     * @param s 字符串
     * @return boolean
     */
    public static boolean isNotEmpty(String s) {
        return null != s && s.length() > 0 && !"".equals(s) && !"null".equals(s);
    }

    public static String requireNonNullOrEmpty(String str) {
        return requireNonNullOrEmpty(str, "str == null or length == 0");
    }

    public static String requireNonNullOrEmpty(String str, String msg) {
        if (!isNotEmpty(str)) {
            throw new IllegalArgumentException(msg);
        }

        return str;
    }

    /**
     * 转化link为map
     *
     * @param str 需要解析的字符串
     * @return 解析后的map
     */
    public static Map<String, Object> getLinkMap(String str) {
        Map<String, Object> map = new HashMap<>(4);
        String[] r = str.split(",");
        for (String s : r) {
            String k = s.substring(0, s.indexOf("="));
            String v = s.substring(s.indexOf("=") + 1, s.length());
            map.put(k, v);
        }
        return map;
    }

    /**
     * 获取url中keyword 或rescourceId或keyword字段值
     * @param linkUrl
     * @return
     */
    public static String getUrlResIdStr(String linkUrl) {
        if (isNotEmpty(linkUrl)) {
            try {
                if (linkUrl.contains(MEDPLUS)) {
                    int index=linkUrl.indexOf("?");
                    String subStr=linkUrl.substring(index+1,linkUrl.length());
                    if(StringUtil.isEmpty(subStr)){
                        return "";
                    }
                    Map<String, Object> map = StringUtil.getLinkMap(subStr.replace(":","="));
                    return  (String) map.get("keyword");
                }else if (linkUrl.contains(YIDING)){
///                    "yiding://cn.net.yiding?data={scene:2,type:2,resourceId:"+rescourceId+"}"
                    int index=linkUrl.indexOf("?");
                    String subStr=linkUrl.substring(index+1,linkUrl.length());
                    if(StringUtil.isEmpty(subStr)){
                        return "";
                    }
                    String replace = subStr.replace("=", ":");
                    int startindex = subStr.indexOf("{");
                    int endindex = subStr.indexOf("}");
                    String substring = replace.substring(startindex + 1, endindex);
                    String[] split = substring.split(",");
                    for (String s : split) {
                        if (s.contains("resourceId")) {
                            return s.substring(s.indexOf(":")+1,s.length());
                        }
                    }

                }else if (linkUrl.startsWith(SCENE)){
                    Map<String, Object> map = StringUtil.getLinkMap(linkUrl);
                    return (String) map.get("keyword");
                }else {
                    return "";
                }
            }catch (Exception e){
                Log.d("Banner get keyword fail!",e.toString());
            }

        }
        return "";
    }


    /**
     *去掉字符串中的空格
     * @param str
     * @return
     */
    public static String trim(String str){
        if (!StringUtil.isEmpty(str)){
            int startIndex=0;
            int endIndex=str.length()-1;
            //当从0界标起截取字符串的字符为空，则界标加一；直到截取的字符不为空循环就停止；
            while(startIndex<endIndex && str.charAt(startIndex) == CHAR_SPACE){startIndex++;}
            //当从最后一个字符的index界标起截取字符串的字符为空，则界标减一；直到截取的字符不为空循环就停止；
            while(startIndex<endIndex &&str.charAt(endIndex) == CHAR_SPACE){endIndex--;}
            String text=str.substring(startIndex, endIndex+1);
            return text;
        }
        return "";
    }
    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }
    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        // 替换中文标号
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":").replaceAll("、",
                ",");
        // 清除掉特殊字符
        String regEx = "[『』]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     * @param  s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int length(String s) {
        if (s == null) {
            return 0;
        }
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }
}
