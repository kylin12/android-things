package com.example.androidthings.simpleui.util;

import android.app.Dialog;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.example.androidthings.simpleui.base.BaseApplication;

import java.util.List;


public class NetUtil {
    private static final String TAG = "NetUtil";

    //    /**
    //     * 获取手机运营商
    //     * @return
    //     */
    //    public static int getNetIsmi(){
    //        TelephonyManager telManager = (TelephonyManager) MyApplication.context().getSystemService(Context
    // .TELEPHONY_SERVICE);
    //        String imsi = telManager.getSubscriberId();
    //        if(imsi!=null){
    //            if(imsi.startsWith("46000") || imsi.startsWith("46002")) {
    //                return DataType.NETWORK_IMSI_MOBILE;
    //            }else if(imsi.startsWith("46001")){
    //                return DataType.NETWORK_IMSI_UNICOM;
    //            }else if(imsi.startsWith("46003")){
    //                return DataType.NETWORK_IMSI_TELECOM;
    //            }
    //        }
    //        return DataType.NETWORK_IMSI_UNICOM;
    //    }

    /**
     * 判断wifi状态
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.isConnected()) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断网络连接状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断移动网络
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null && mMobileNetworkInfo.isConnected()) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取连接类型
     *
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }


    public static Dialog progressDialog;

    /**
     * 判断网络是否链接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) BaseApplication.context().getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        return !(networkinfo == null || !networkinfo.isAvailable());
    }

    /**
     * 判断GPS是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = lm.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    /**
     * 判断WIFI是否打开
     *
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo
                .State.CONNECTED) || mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断是否是3G网络
     *
     * @param context
     * @return
     */
    public static boolean is3rd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 判断是wifi还是3g网络,用户的体现性在这里了，wifi就可以建议下载或者在线播放。
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 获取手机ip
     *
     * @param context
     * @return
     */
    //    public static String getIp(Context context) {
    //        String ipStr = "";
    //        if (com.allin.social.comm.utils.http.NetUtil.isWifiEnabled(context) || com.allin.social.comm.utils.http
    // .NetUtil.isGpsEnabled(context)) {
    //            if (com.allin.social.comm.utils.http.NetUtil.isNetworkAvailable(context)) {
    //                if (com.allin.social.comm.utils.http.NetUtil.isWifi(context)) {
    //                    ipStr = com.allin.social.comm.utils.http.NetUtil.getIpForWifi(context);
    //                } else {
    //                    ipStr = com.allin.social.comm.utils.http.NetUtil.getLocalIpAddress();
    //                }
    //            }
    //        }
    //        return ipStr;
    //    }

    /**
     * 获取手机外网ip
     *
     * @param context
     * @return
     */
    //    public static String getNetIp(Context context) {
    //        try {
    //            OkHttpUtil.get("http://ip.6655.com/ip.aspx", new ResultCallback<String>() {
    //                @Override
    //                public void onError(Request request, Exception e, int id) {
    //                    Log.e(TAG, "-------ip URL+获取外网ip错误.request="+request + "    exception"+ e.getMessage());
    //                }
    //
    //                @Override
    //                public void onSuccessResponse(String responseString) {
    //                    Log.i(TAG, "获取外网ip==" + responseString);
    //                    Log.i(TAG, "获取外网ip成功");
    ///*                  Pattern pattern = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}
    // (?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
    //                    Matcher matcher = pattern.matcher(responseString);
    //                    if (matcher.find()) {
    //                    }*/
    //                    Const.NET_IP = responseString;
    //                    LibApp.setmOpIp(responseString);
    //                }
    //            });
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return "";
    //    }

    /**
     * wifi状态下获取手机ip ,需要以下权限 <uses-permission
     * android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * <uses-permission
     * android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
     * <uses-permission
     * android:name="android.permission.WAKE_LOCK"></uses-permission>
     *
     * @param context
     * @return
     */
    public static String getIpForWifi(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    /**
     * 3g网络先获取手机ip
     *
     * @return
     */
    //    public static String getLocalIpAddress() {
    //        try {
    //            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements
    // (); ) {
    //                NetworkInterface intf = en.nextElement();
    //                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements
    // (); ) {
    //                    InetAddress inetAddress = enumIpAddr.nextElement();
    //                    if (!inetAddress.isLoopbackAddress()) {
    //                        return inetAddress.getHostAddress().toString();
    //                    }
    //                }
    //            }
    //        } catch (SocketException ex) {
    //            Log.e(TAG, "WifiPreference IpAddress" + ex.toString());
    //        }
    //        return null;
    //    }

    /**
     * 创建并显示网络访问加载进度dialog
     */
    //    public static void createProDialog(Context context) {
    //        if (progressDialog == null) {
    //            progressDialog = new Dialog(context, R.style.progress_dialog);
    //            progressDialog.setContentView(R.layout.dialog);
    //            progressDialog.setCancelable(false);
    //            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    //            progressDialog.show();
    //        } else {
    //            progressDialog.show();
    //        }
    //    }

    /**
     * 关闭进度圈
     */
    public static void closeDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        progressDialog = null;
    }

    /**
     * 将URL中的特殊字符进行转译
     *
     * @param url
     * @return
     */
    public static String urlReplace(String url) {
        if (url.contains("{")) {
            url = url.replaceAll("\\{", "%7b");
        }
        if (url.contains("}")) {
            url = url.replaceAll("\\}", "%7d");
        }
        if (url.contains("\"")) {
            url = url.replaceAll("\"", "%22");
        }
        return url;
    }

}
