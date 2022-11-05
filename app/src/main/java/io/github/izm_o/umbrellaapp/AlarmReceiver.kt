package io.github.izm_o.umbrellaapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
//import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

//PendingIntentからのIntentを受け取るクラス
class AlarmReceiver : BroadcastReceiver() {

    //AlarmReceiver.ktの複数のメソッドからアクセスできるように、chance1～4をプロパティにする。
    private var chance1: String? = null
    private var chance2: String? = null
    private var chance3: String? = null
    private var chance4: String? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        //spinner2で選んだ値に対応するコードを受け取る
        val sharedPref = context?.getSharedPreferences("DataStore2", Context.MODE_PRIVATE)
        val myItem = sharedPref?.getString("InputCode","null")

        //天気情報取得
        val mainUrl = "https://weather.tsukumijima.net/api/forecast/city/"
        //お天気URLを取得して
        val weatherUrl = "$mainUrl$myItem"
        //そのURLを元に得られた情報の結果を表示
        weatherTask(weatherUrl)//この中身は下へ

        //DestinationActivity.ktへ値を渡す
        val chanceOR1 = chance1
        val chanceOR2 = chance2
        val chanceOR3 = chance3
        val chanceOR4 = chance4
        val sharedPref2 = context?.getSharedPreferences("DataStore3",Context.MODE_PRIVATE)
        val editor = sharedPref2?.edit()
        editor?.putString("InputRain1", chanceOR1)
        editor?.putString("InputRain2", chanceOR2)
        editor?.putString("InputRain3", chanceOR3)
        editor?.putString("InputRain4", chanceOR4)
        editor?.apply()

        val i = Intent(context,DestinationActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_IMMUTABLE)

        //NotificationManager
        val builder = NotificationCompat.Builder(context!!,"TEST_CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("本日の降水確率")
            .setContentText("0～6時 : $chanceOR1、6～12時 : $chanceOR2、12～18時 : $chanceOR3、18～24時 : $chanceOR4")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("0～6時 : $chanceOR1、6～12時 : $chanceOR2、12～18時 : $chanceOR3、18～24時 : $chanceOR4"))
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123,builder.build())

        //アラームの繰り返し
        setNextAlarmService(context)
    }

    //アラームの繰り返し
    private fun setNextAlarmService(context: Context?) {
        // 24時間毎のアラーム設定
        val repeatPeriod = (24 * 60 * 60 * 1000).toLong()//24時間は(24 * 60 * 60 * 1000)
        val intent = Intent(context, AlarmReceiver::class.java)
        val startMillis = System.currentTimeMillis() + repeatPeriod
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent,PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context!!.getSystemService(Service.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
            startMillis, pendingIntent)
    }

    //天気情報取得
    //weatherTask()の中身
    private fun weatherTask(url1: String) {
        //コルーチンスコープを用意
        runBlocking{
            //HTTP通信（ワーカースレッド）
            val result = weatherBackgroundTask(url1)
            //お天気データ(JSONデータ)を表示（UIスレッド）
            weatherJsonTask(result)
        }
    }

    //HTTP通信（ワーカースレッド）の中身(※suspend=中断する可能性がある関数につける)
    private suspend fun weatherBackgroundTask(url2: String): String {
        //withContext=スレッドを分離しますよ、Dispatchers.IO=ワーカースレッドで実行しますよってこと
        val response = withContext(Dispatchers.IO) {
            //天気情報サービスから取得した結果情報（JSON文字列）を後で入れるための変数（いったん空っぽ）を用意。
            var httpResult = ""
            //  try{エラーがあるかもしれない処理を実行}catch{実際エラーがあった場合}
            try {
                val urlObj = URL(url2)//ただのURL文字列をURLオブジェクトに変換（文字列にリンクを付けるイメージ）
                // アクセスしたサイトから情報を取得
                //テキストファイルを読み込むクラス(文字コードを読めるようにする準備(URLオブジェクト))
                val br = BufferedReader(InputStreamReader(urlObj.openStream()))
                //読み込んだデータを文字列に変換して代入
                //httpResult =br.toString()　この書き方だとエラーでるよ
                httpResult = br.readText()
            } catch (e: IOException) {//IOExceptionとは例外管理するクラス
                e.printStackTrace()//エラーが発生したよって言う
            } catch (e: JSONException) {//JSONデータ構造に問題が発生した場合の例外
                e.printStackTrace()
            }
            //HTTP接続の結果、取得したJSON文字列httpResultを戻り値とする
            //withContext()の場合はreturnの後に@withContextをつけないといけない
            return@withContext httpResult
        }
        return response
    }

    //weatherJsonTask()の中身
    private  fun weatherJsonTask(rlt:String){
        // まずは取得した、JSONオブジェクト一式を生成。
        val jsonObj = JSONObject(rlt)
        // JSONオブジェクトの、JSON配列オブジェクトを取得。
        val weatherJSONArray =jsonObj.getJSONArray("forecasts")
        // JSONオブジェクト(配列の0番目)を取得。
        val weatherJSON =weatherJSONArray.getJSONObject(0)
        val weather =weatherJSON.getJSONObject("chanceOfRain")
        val rain1 = weather.getString("T00_06")
        val rain2 = weather.getString("T06_12")
        val rain3 = weather.getString("T12_18")
        val rain4 = weather.getString("T18_24")
        chance1 = rain1
        chance2 = rain2
        chance3 = rain3
        chance4 = rain4
    }
    //天気情報取得ここまで
}