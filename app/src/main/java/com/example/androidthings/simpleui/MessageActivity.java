/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.simpleui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidthings.simpleui.base.BaseApplication;
import com.example.androidthings.simpleui.entity.BaseResponseObject;
import com.example.androidthings.simpleui.util.OkHttpUtil;
import com.example.androidthings.simpleui.util.StringUtil;
import com.example.androidthings.simpleui.util.WebFrontUtil;
import com.zhy.http.okhttp.callback.ResultCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class MessageActivity extends Activity {

    private static final String TAG = MessageActivity.class.getSimpleName();

    private EditText mobile;
    private TextView userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && bundle.size() > 0) {
                String data = bundle.getString("data");
                Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
            }
        }
        setContentView(R.layout.activity_message);
        mobile=(EditText)findViewById(R.id.mobile);
        userInfo=(TextView)findViewById(R.id.userInfo);

    }

    public void loadUserInfo(View view){
        String mobile =this.mobile.getText().toString();
        mobile= StringUtil.isEmpty(mobile)?"18201292086":mobile;
        Map<String, Object> params = new HashMap<>();
        //params.put("customerId", "1397586887524");//1434505803474  线下  1444793984250线上
        params.put("mobile", mobile);
        params.put("visitSiteId", "6");
        params.put("pageIndex", 1);
        params.put("pageSize", 1);
        WebFrontUtil.handlePage(params);
        //services;
        String url = "http://192.168.1.32:18080/customer/unite/getByParam";
        OkHttpUtil.get(url, params, new ResultCallback<String>() {
            @Override
            public void onBefore(Request request, int id) {
                Toast.makeText(BaseApplication.context(),"start",Toast.LENGTH_LONG).show();
                super.onBefore(request,id);
            }

            @Override
            public void onError(Request request, Exception e, int id) {
                Toast.makeText(BaseApplication.context(),"出错了："+e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String responseString, int id) {
                Log.i(TAG, "docList.res==" + responseString);
                BaseResponseObject responseObject = WebFrontUtil.getResponseObject(responseString);
                boolean status = responseObject.getResponseStatus();

                if (status && responseObject!=null) {
                    HashMap map = (HashMap)responseObject.getResponseData().get("customerUnite");
                    StringBuilder result=new StringBuilder();
                    result.append("customerID："+StringUtil.getMapString(map,"customerId")+"\r\n");
                    result.append("mobile："+StringUtil.getMapString(map,"mobile")+"\r\n");
                    result.append("email："+StringUtil.getMapString(map,"email")+"\r\n");
                    result.append("nickname："+StringUtil.getMapString(map,"nickname")+"\r\n");
                    result.append("role："+StringUtil.getMapString(map,"customerRole")+"\r\n");
                    result.append("time："+StringUtil.getMapString(map,"createTime"));
                    userInfo.setText(result.toString());
                } else {
                    if (responseObject== null) {
                        Toast.makeText(BaseApplication.context(), "空数据", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BaseApplication.context(), "获取数据错误", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onAfter(String code, int id) {
                super.onAfter(code,id);
                Toast.makeText(MessageActivity.this,"AFTER："+code,Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
