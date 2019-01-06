package com.example.administrator.sensortest;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.Provider;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager manager;
    private Sensor mSensorOrientation;
    private Vibrator mVibrator;

    private TextView t1;
    private TextView t2;
    private TextView t3;

    private ImageView imageView;
    private float a = 0.0f ;
    private float b = 0.0f ;
    private float c = 0.0f ;
    private boolean mV = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (SensorManager) getApplication().getSystemService(SENSOR_SERVICE);
        mSensorOrientation = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        manager.registerListener(this,mSensorOrientation,SensorManager.SENSOR_DELAY_UI);
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        bindView();

    }

    private void bindView() {
         t1 = findViewById(R.id.t1);
         t2 = findViewById(R.id.t2);
         t3 = findViewById(R.id.t3);
         imageView = findViewById(R.id.image);
    }

    public void onBtClick(View v){
        mV = !mV;
    }

    public void onBtClickCapture(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,Activity.DEFAULT_KEYS_DIALER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode == Activity.RESULT_OK){
           Bundle mbundle = data.getExtras();
           Bitmap bitmap = (Bitmap) mbundle.get("data");
           imageView.setImageBitmap(bitmap);
           Log.i("xian","----");

       }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        t1.setText("方位角：" + (float)(Math.round(event.values[0] * 100)) / 100);
        t2.setText("倾斜角：" + (float)(Math.round(event.values[1] * 100)) / 100);
        t3.setText("滚动角：" + (float)(Math.round(event.values[2] * 100)) / 100);


        Log.i("bin" ,"滚动角：" + (float)(Math.round(event.values[2] * 100)) / 100 );

        float aa = (float)(Math.round(event.values[0] * 100)) / 100;
        float bb = (float)(Math.round(event.values[1] * 100)) / 100;
        float cc = (float)(Math.round(event.values[2] * 100)) / 100;

        if (Math.abs(a - aa) > 20 || Math.abs(b - bb ) > 20 || Math.abs(c - cc ) > 20){

            mVibrator.cancel();
            if(mV){
                mVibrator.vibrate(new long[]{100, 100, 100, 1000}, 0);
            }

            Log.i("bin" , "震动");
        }

        a = aa;
        b = bb;
        c = cc;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
