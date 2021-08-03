package io.nkmr.httpiida.koara;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.widget.ImageView;

import java.io.DataOutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

////line送信：目標達成ならず、かつ歩行速度クリアVer


public class App2 extends Thread {

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



    private String text2 = "";  //text2で揃えると48行目に歩行速度等を反映できるかも？　　今回歩いた"+ text +"m/秒でした。

    public App2(String arg) {

        text2 = arg;
    }

    public void run() {
        try {
            URL obj = new URL("https://api.line.me/v2/bot/message/push");
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // HTTPヘッダを設定
            con.setRequestProperty("Authorization", "Bearer " + アクセストークン);
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

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
