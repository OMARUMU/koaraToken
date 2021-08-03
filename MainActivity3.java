package io.nkmr.httpiida.koara;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

////目標達成ならず、かつ歩行速度クリアVer


public class MainActivity3 extends AppCompatActivity {

    ImageView imageView;

    SharedPreferences data;
    int count = 0;
    boolean started = false;
    float degree = 0;
    double speed = 0;
    SensorManager sensorManager;
    Sensor stepDetectorSensor;
    // ロケーション機能定義
    private LocationManager mLocationManager;
    private String bestProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
/*
        ////intentのデータを取得
        Intent intent =getIntent();
        String count = intent.getStringExtra(MainActivity.data);
        ////TextViewに表示
        TextView countView=findViewById(R.id.countView);
        countView.setText(String.valueOf(count));

 */

        // 目標歩数未達成の時、line notifyへ未達成用のテキスト送信
        ////SharedPreferences からデータを取得
        data = getSharedPreferences("Data", MODE_PRIVATE);
        int count = data.getInt("count", 0);
        int sokudo = data.getInt("sokudo", 0);

        if (count < 30 && sokudo >=1.0) {   //// 目標歩数達成ならず、かつ最低速度が1.0m/秒の時、line notifyへ目標達成かつMCI可能性低い旨のテキスト送信


            ////TextViewに表示
            TextView countView = findViewById(R.id.countView);
            countView.setText(String.valueOf(count));
            TextView speedView = findViewById(R.id.speedView);
            speedView.setText(String.valueOf(sokudo));


/*            App2 app2 = new App2("歩数は" + count + "、歩行速度は" + sokudo);
            app2.start();
 */




        }


            Button returnButton = findViewById(R.id.resetButton);
            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
    }
