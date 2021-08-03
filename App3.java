package io.nkmr.httpiida.koara;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.widget.ImageView;

import java.io.DataOutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;


////line送信：目標達成かつ歩行速度クリアVer


public class App3 extends Thread {



    ImageView imageView;

    SharedPreferences data;
    int count=0;
    boolean started=false;
    float degree=0;
    double speed=0;
    SensorManager sensorManager;
    Sensor stepDetectorSensor;
    // ロケーション機能定義
    private LocationManager mLocationManager;
    private String bestProvider;
////

    private String text3 = "";

    public App3(String arg) {

        text3 = arg;
    }

    public void run() {
        try {
            URL obj = new URL("https://api.line.me/v2/bot/message/push");
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // HTTPヘッダを設定
            con.setRequestProperty("Authorization", "Bearer " + アクセストークン);
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

/*
            data=getSharedPreferences("Data",MODE_PRIVATE);
            int count = data.getInt("count",0);
            int sokudo = data.getInt("sokudo",0);
*/


            // POSTデータを設定
            String post = "{\"to\":\ユーザーID,\"messages\":[{\"type\":\"text\",\"text\":\"目標達成おめでとうございます！ 今回歩いた"+ text +"m/秒でした。認知症の可能性は低いです。ところで、列車の旅はいかがですか？　基礎体力づくりにウォーキングをがんばりましょう！ https://youtu.be/SfHwvpkNS-w\"}]},";

            // 実行
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            DataOutputStream outStrm = new DataOutputStream(con.getOutputStream());

            byte[] buf = post.getBytes("UTF-8");
            outStrm.write(buf, 0, buf.length);

            outStrm.flush();
            outStrm.close();

            // HTTPステータスが 200 だったら OK
            int httpStatus = con.getResponseCode();
            String resMsg = con.getResponseMessage();
            System.out.println(httpStatus + ":" + resMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
