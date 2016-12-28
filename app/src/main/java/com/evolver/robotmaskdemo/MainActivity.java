package com.evolver.robotmaskdemo;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.RobotMaskManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{//
    Button open;
    Button close;
    Button half;
    Button speedUp;
    Button speedDown;
    //Button off;
    Button reset;
    short position;
    short speed;
    RobotMaskManager robotMaskManager;
    private IntentFilter intentFilter;
    private MyBroadcastReceiver myBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        open = (Button) findViewById(R.id.open);
        close = (Button) findViewById(R.id.close);
        half = (Button) findViewById(R.id.HALF);
        speedUp = (Button) findViewById(R.id.SPEEDUP);
        speedDown = (Button) findViewById(R.id.SPEEDDOWN);
      //  off = (Button) findViewById(R.id.OFF);
        reset = (Button) findViewById(R.id.RESET);

        robotMaskManager = (RobotMaskManager) getSystemService(Context.ROBOT_MASK_SERVICE);
        //robotMaskService.init(robotMaskManager);
        open.setOnClickListener(this);
        close.setOnClickListener(this);
        half.setOnClickListener(this);
        speedUp.setOnClickListener(this);
        speedDown.setOnClickListener(this);
      //  off.setOnClickListener(this);
        reset.setOnClickListener(this);
        speed = 50;

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_EVOLVER_MASK_STATUS_CHANGE);
        intentFilter.addAction(Intent.ACTION_ROBOT_MASK_FAULT);
        intentFilter.addAction("android.intent.action.MASK_KEY");
        myBroadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver,intentFilter);
        robotMaskManager.powerOn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open:
                position = 90;
                if(robotMaskManager.setPositionSpeed(position, speed) < 0)
                    Toast.makeText(this,  "open error", Toast.LENGTH_SHORT).show();
                break;
            case R.id.close:
                position = 0;
                if(robotMaskManager.setPositionSpeed(position, speed) < 0)
                    Toast.makeText(this,  "close error", Toast.LENGTH_SHORT).show();
                //Toast.makeText(this,"state = " + robotMaskManager.getState(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.SPEEDUP:
                speed += 10;
                if(speed >= 90) {
                    speed = 100;
                    Toast.makeText(this,"最大速度",Toast.LENGTH_SHORT).show();
                }
                if(robotMaskManager.setPositionSpeed(position, speed) < 0)
                    Toast.makeText(this,  "set speed error", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"speed = " + speed,Toast.LENGTH_SHORT).show();
                break;
            case R.id.SPEEDDOWN:
                speed -= 10;
                if(speed <= 10) {
                    speed = 0;
                    Toast.makeText(this,"最小速度",Toast.LENGTH_SHORT).show();
                }
                if(robotMaskManager.setPositionSpeed(position, speed) < 0)
                    Toast.makeText(this,  "set speed error", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"speed = " + speed,Toast.LENGTH_SHORT).show();
                break;
            case R.id.HALF:
                position = 60;
                if(robotMaskManager.setPositionSpeed(position, speed) < 0)
                    Toast.makeText(this,  "error", Toast.LENGTH_SHORT).show();
             //   Toast.makeText(this,"machine = " + new String(robotMaskManager.getMachine()),Toast.LENGTH_SHORT).show();
                break;
//            case R.id.OFF:
//                //position = 0;
//               // speed = 0x0;
//                robotMaskManager.powerOff();
//                break;
            case R.id.RESET:
                robotMaskManager.powerReset();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
        // robotMaskManager.release();
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_EVOLVER_MASK_STATUS_CHANGE)) {
                boolean name = intent.getBooleanExtra("KEYCODE_MASK_OPEN", false);
               // Log.e("glei", "onReceive +++++++ " + name);
                //Toast.makeText(context, name + "", Toast.LENGTH_SHORT).show();
            } else if (action.equals(Intent.ACTION_ROBOT_MASK_FAULT)) {

            }
            else if (action.equals("android.intent.action.MASK_KEY")) {
                //Toast.makeText(context, "mask key " + robotMaskManager.getSwitch() , Toast.LENGTH_SHORT).show();
                switch (robotMaskManager.getSwitch()) {
                    case 1://open
                        position = 0;
                        robotMaskManager.setPositionSpeed(position,speed);
                        Toast.makeText(context,  "open", Toast.LENGTH_SHORT).show();
                        break;
                    case 2://onprogress
                        //position = 0;
                        //robotMaskManager.setPositionSpeed(position,speed);
                        Toast.makeText(context,  "onprogress", Toast.LENGTH_SHORT).show();
                        break;
                    case 3://close
                        position = 90;
                        robotMaskManager.setPositionSpeed(position,speed);
                        Toast.makeText(context,  "close", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(context,  "error", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
//            position = 90;
//            robotMaskService.setPositionSpeed(position, speed);
        }}
}

