package io.nkmr.httpiida.koara;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;


import java.io.DataOutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    ImageView imageView;

    SharedPreferences data;
    int count=0;
    int sokudo=0;
    boolean started=false;

    float degree=0;
    double speed=0;

    SensorManager sensorManager;
    Sensor stepDetectorSensor;

    // ロケーション機能定義
    private LocationManager mLocationManager;
    private String bestProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        //////目標歩数入力フォーム
        final EditText editText = findViewById(R.id.edit_text);


        final String[] str = new String[1];


        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean flag) {
                if(!flag){

                    str[0] = editText.getText().toString().trim();  //public String trim() 文字列から前後の空白を除去するには、trimメソッドを利用します。ここで言う空白には、半角スペースだけでなく、改行文字やタブ文字も含まれます（ただし、全角スペースは含まれません）。


                    int hosuu = Integer.parseInt(str[0]);

                    if( hosuu<0 || 10000<hosuu){
                        Toast toast = Toast.makeText(MainActivity.this,"正しい入力値を入れてください",Toast.LENGTH_LONG);
                        toast.show();
                    }//89行目からの{に対応
                }
            }

        });
//////





        // ロケーションマネージャ初期化
        initLocationManager();

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        stepDetectorSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        sensorManager.registerListener(this,stepDetectorSensor,sensorManager.SENSOR_DELAY_NORMAL);

        data=getSharedPreferences("Data",MODE_PRIVATE);
        started=data.getBoolean("started",false);
        count=data.getInt("count",0);
        speed=count*0.01;

        Button startButton=findViewById(R.id.startButton);
        if(!started) {
            startButton.setText("START");
        }else{
            startButton.setText("STOP");
        }
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!started) {
                    ((TextView) v).setText("STOP");
                    Intent intent = new Intent(getApplication(), CountService.class);
                    startForegroundService(intent);
                    // STARTに合わせてロケーション機能も開始
                    locationStart();
                } else {
                    ((TextView) v).setText("START");
                    Intent intent = new Intent(getApplication(), CountService.class);
                    stopService(intent);
                    // STOPに合わせてロケーション機能も停止
                    locationStop();




                    ////目標達成かつ歩行速度クリアならずVer　機能している


                    if (count >= Integer.parseInt(str[0])) {   //////入力フォーム（60~70行目）の値をint型に変換してcountと比較

                        //if (count >= 30 ) {
                        // 目標歩数達成の時、line notifyへ達成用のテキスト送信

                        data=getSharedPreferences("Data",MODE_PRIVATE);
                        int count = data.getInt("count",0);
                        int sokudo = data.getInt("sokudo",0);

                        if (sokudo <1.0) {

                            App3 app3 = new App3("歩数は" + count + "、歩行速度は" + sokudo);
                            app3.start();

                            ////STOPに合わせて画面遷移,同時に歩数と歩行速度を遷移先に渡す
                            ////startButton.setOnClickListener(v -> {
                            intent = new Intent(getApplication(), MainActivity4.class);
                            //intent.putExtra(data.count);
                            //intent.putExtra(data.speed);

                            startActivity(intent);

                        }

                        else {
                            App app = new App("歩数は" + count + "、歩行速度は" + sokudo);
                            app.start();


                            ////STOPに合わせて画面遷移,同時に歩数と歩行速度を遷移先に渡す
                            ////startButton.setOnClickListener(v -> {
                            intent = new Intent(getApplication(), MainActivity2.class);
                            //intent.putExtra(data.count);
                            //intent.putExtra(data.speed);

                            startActivity(intent);


                        }


                    }





////目標達成ならず、かつ歩行速度クリアならずVer　機能中



                    if (count < Integer.parseInt(str[0])) {   //////入力フォーム（60~70行目）の値をint型に変換してcountと比較
                        //if (count < 30 ) {
                        // 目標歩数達成の時、line notifyへ達成用のテキスト送信

                        data=getSharedPreferences("Data",MODE_PRIVATE);
                        int count = data.getInt("count",0);
                        int sokudo = data.getInt("sokudo",0);

                        if(sokudo <1.0) {

                            App4 app4 = new App4("歩数は" + count + "、歩行速度は" + sokudo);
                            app4.start();

                            ////STOPに合わせて画面遷移,同時に歩数と歩行速度を遷移先に渡す
                            ////startButton.setOnClickListener(v -> {
                            intent = new Intent(getApplication(), MainActivity5.class);
                            //intent.putExtra(data.count);
                            //intent.putExtra(data.speed);

                            startActivity(intent);

                        }


                        else {
                            App2 app2 = new App2("歩数は" + count + "、歩行速度は" + sokudo);
                            app2.start();


                            ////STOPに合わせて画面遷移,同時に歩数と歩行速度を遷移先に渡す
                            ////startButton.setOnClickListener(v -> {
                            intent = new Intent(getApplication(), MainActivity3.class);
                            //intent.putExtra(data.count);
                            //intent.putExtra(data.speed);

                            startActivity(intent);

                        }


                    }



                }
                started = !started;
                SharedPreferences.Editor editor = data.edit();
                editor.putBoolean("started", started);
                editor.apply();


            }

        });

        Button resetButton=findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=data.edit();
                editor.putInt("count",0);
                editor.apply();
            }
        });

        TextView countView=findViewById(R.id.countView);
        countView.setText(String.valueOf(count));
        // GPSの速度表示で使用するためコメント
        //TextView speedView=findViewById(R.id.speedView);
        //speedView.setText(String.valueOf(speed));

        handler.post(rRotate);//Rotateクラスは回転に関係する。
        handler.post(rUpdate);
    }

    Handler handler=new Handler(); //Handlerクラスの正しい使い方（Androidでスレッド間通信） https://sankumee.hatenadiary.org/entry/20120329/1333021847

    Runnable rRotate=new Runnable() {
        @Override
        public void run() {  //Runnableを実装するクラスは、Threadのインスタンスを生成し、ターゲットとしてクラス自身を渡すことによりThreadをサブクラス化をしなくても実行できます。多くの場合、Threadメソッドのうち、run()メソッドだけをオーバーライドして使用する場合は、Runnableインタフェースを使用してください。これは、クラスの基本的な動作を修正または拡張するのでない限り、そのクラスをサブクラス化することは好ましくないため、重要です。

            imageView=findViewById(R.id.imageView);
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.koara);  //BitmapFactoryクラスは外部ファイルやリソース、ストリームなどからBitmapクラスのオブジェクトを作成するためのクラスです。https://www.javadrive.jp/android/bitmap/


            Matrix matrix=new Matrix();
            matrix.setRotate(degree,bitmap.getWidth()/2,bitmap.getHeight()/2);
            Bitmap rotatedBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);  //指定された幅、高さ、色形式を持つビットマップを作成します。この関数は、モノクロビットマップの作成に使用します。http://chokuto.ifdef.jp/urawaza/api/CreateBitmap.html


            imageView.setImageBitmap(rotatedBitmap);

            degree+=speed;

            handler.postDelayed(this,1); //「１秒後に画像を切り替えたい」とか、そういう要件に使用します。こういうときはHandlerクラスのpostDelayedを呼べば実現可能です。new Handler().postDelayed( <Runnableオブジェクト>, <処理を呼び出すまでの時間(ミリ秒)>); https://dev.classmethod.jp/articles/android-tas/
        }
    };
    Runnable rUpdate=new Runnable() {
        @Override
        public void run() {
            count=data.getInt("count",0);
            speed=count*0.01;

            TextView countView=findViewById(R.id.countView);
            countView.setText(String.valueOf(count));
            // GPSの速度表示で使用するためコメント
            //TextView speedView=findViewById(R.id.speedView);
            //speedView.setText(String.valueOf(speed));

            handler.postDelayed(this,500);
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType()==Sensor.TYPE_STEP_DETECTOR){
        }
    }

    // 以下、ロケーション機能で追加したソースコード
    private void initLocationManager() {
        // インスタンス生成
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 詳細設定
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setSpeedRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        bestProvider = mLocationManager.getBestProvider(criteria, true);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // パーミッションの許可を取得する
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
    }

    private void locationStart() {
        checkPermission();
        mLocationManager.requestLocationUpdates(bestProvider, 1000, 1, this);
    }

    private void locationStop() {
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {    //abstract void  onLocationChanged(Location location) 場所が変更されたときに呼び出されます https://developer.android.com/reference/android/location/LocationListener
        Log.d("DEBUG", "called onLocationChanged");
        Log.d("DEBUG", "lat : " + location.getLatitude());
        Log.d("DEBUG", "lon : " + location.getLongitude());

        double min_sokudo = 0.0;
        double sokudo = 0.0;
        //速度が出ている？
        if(location.hasSpeed()) {
            //速度が出ている時（km/hに変換して変数sokudoへ）
            sokudo = location.getSpeed();// * 3.6f;　　移動速度を取得するにはLocationクラスの「getSpeed」メソッドを利用する。取得できるのは「メートル／秒（m/s）」のFloatとなる。

            //Float sokudo;
            //sokudo = location.getSpeed();

            // min_sokudo が初期化されていた場合は初期値として現在の sokudo 値を入れる
            if (min_sokudo == 0) {
                min_sokudo = sokudo;
            }

            // 現在の sokudo が min_sokudo を下回ったら、min_sokudo を更新する
            if (sokudo < min_sokudo) {
                min_sokudo = sokudo;
            }
        } else {
            //速度が出ていない時
            sokudo = 0.0;
        }
        //速度を表示する
        //数値を切り上げた値を取得するには Math クラスで用意されている ceil メソッドを使います。 ceil メソッドはクラスメソッドです。
        TextView speedView=findViewById(R.id.speedView);  //public static double ceil(double a)  https://www.javadrive.jp/start/math/index7.html
        speedView.setText(Math.ceil(sokudo) + " m/秒");  //注: Math.ceil(null) は整数の 0 を返し、 NaN エラーは返しません。 https://developer.mozilla.org/ja/docs/Web/JavaScript/Reference/Global_Objects/Math/ceil

        ////sokudo保存
        SharedPreferences.Editor editor=data.edit();  //
        editor.putInt("sokudo", (int) sokudo);
        editor.apply();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("DEBUG", "called onStatusChanged");
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("DEBUG", "AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("DEBUG", "OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("DEBUG", "TEMPORARILY_UNAVAILABLE");
                break;
            default:
                Log.d("DEBUG", "DEFAULT");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("DEBUG", "called onProviderDisabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("DEBUG", "called onProviderEnabled");
    }
}
