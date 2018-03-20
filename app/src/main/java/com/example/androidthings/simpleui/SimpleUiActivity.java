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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.Pwm;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleUiActivity extends Activity {

    private static final String TAG = SimpleUiActivity.class.getSimpleName();
    private static final String BLINK_BCM="BCM5";
    private static final String BUTTON_BCM="BCM21";
    private static final String BUTTON_BCM2="BCM20";

    private Map<String, Gpio> mGpioMap = new LinkedHashMap<>();
    private Handler blinkHandler = new Handler();
    private boolean blinkState = false;
    private Gpio blinkGpio;
    private Gpio buttonGpio;
    private Gpio buttonOutGpio;
    private boolean buttonOutState = false;

    private Pwm pwm1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout gpioPinsView = (LinearLayout) findViewById(R.id.gpio_pins);
        LayoutInflater inflater = getLayoutInflater();
        PeripheralManagerService pioService = new PeripheralManagerService();
        //PeripheralManager pioService=PeripheralManager.getInstance();
        try {
            for (String name : pioService.getGpioList()) {
                View child = inflater.inflate(R.layout.list_item_gpio, gpioPinsView, false);
                Switch button = (Switch) child.findViewById(R.id.gpio_switch);
                button.setText(name);
                gpioPinsView.addView(button);

                    Log.d(TAG, "Added button for GPIO: " + name);
                    if("BCM19".equals(name) || "BCM26".equals(name)){
                        final Gpio ledPin = pioService.openGpio(name);
                        ledPin.setEdgeTriggerType(Gpio.EDGE_NONE);//状态生成回调事件的触发类型
                        ledPin.setActiveType(Gpio.ACTIVE_HIGH);
                        ledPin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                try {
                                    ledPin.setValue(isChecked);
                                } catch (IOException e) {
                                    Log.e(TAG, "error toggling gpio:", e);
                                    buttonView.setOnCheckedChangeListener(null);
                                    // reset button to previous state.
                                    buttonView.setChecked(!isChecked);
                                    buttonView.setOnCheckedChangeListener(this);
                                }
                            }
                        });

                        mGpioMap.put(name, ledPin);

                    }else if(BLINK_BCM.equals(name)){
                        blinkGpio=pioService.openGpio(name);
                        blinkGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                        blinkHandler.post(mBlinkRunnable);
                        mGpioMap.put(name, blinkGpio);
                    }else if(BUTTON_BCM.equals(name)){
                        buttonGpio=pioService.openGpio(name);
                        buttonGpio.setDirection(Gpio.DIRECTION_IN);
                        buttonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);

                        buttonOutGpio=pioService.openGpio(BUTTON_BCM2);
                        buttonOutGpio.setActiveType(Gpio.ACTIVE_HIGH);
                        buttonOutGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

                        buttonGpio.registerGpioCallback(new GpioCallback() {
                            @Override
                            public boolean onGpioEdge(Gpio gpio) {
                                try{
                                    buttonOutState=!buttonOutState;
                                    buttonOutGpio.setValue(buttonOutState);
                                    Log.i(TAG, "GPIO changed, button pressed"+gpio.getValue());
                                }catch (IOException e){
                                    Log.e(TAG, "Error button on PeripheralIO API", e);
                                }
                                return true;
                            }
                        });
                        mGpioMap.put(name, buttonGpio);
                        mGpioMap.put(name, buttonOutGpio);


                    }

            }

            //PWM控制LED闪烁频率
            pwm1=pioService.openPwm("PWM1");
            pwm1.setPwmFrequencyHz(0.5);
            pwm1.setPwmDutyCycle(80.0);
            pwm1.setEnabled(true);
        } catch (IOException e) {
            Log.e(TAG, "Error initializing GPIO: ",e);
            // disable button
            //button.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            for (Map.Entry<String, Gpio> entry : mGpioMap.entrySet()) {
                entry.getValue().close();
            }
            mGpioMap.clear();

            blinkHandler.removeCallbacks(mBlinkRunnable);
            blinkGpio = null;

            buttonGpio = null;

            pwm1.close();
        } catch (IOException e) {
            Log.e(TAG, "Error closing GPIO ",e);
        }

    }


    /**
     *
     * 跳转页面
     * @param view
     */
    public void toSet(View view){
        Intent intent = new Intent();
        intent.setClass(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("data", "ddddddddd");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            if(blinkGpio==null){
                return;
            }
            try{
                blinkState=!blinkState;
                blinkGpio.setValue(blinkState);
                blinkHandler.postDelayed(mBlinkRunnable,1000);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    };
}
