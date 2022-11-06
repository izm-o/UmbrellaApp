package io.github.izm_o.umbrellaapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.*
import io.github.izm_o.umbrellaapp.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    //viewBinding
    private lateinit var binding: ActivityMainBinding

    //TimePicker
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar

    //AlarmManager
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    //MainActivityの複数のメソッドからアクセスできるように、keyをプロパティにする。
    private var key: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //Android 8.0 以上では、通知を送信する前に通知チャネルを作成する必要がある。アプリが起動したらすぐにこのコードを実行する必要がある。
        createNotificationChannel()

        //spinner2つの設定
        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.array1, android.R.layout.simple_spinner_dropdown_item
        )
        binding.spinner1.adapter = adapter1
        binding.spinner1.onItemSelectedListener = this

        //プライバシーポリシーのリンク
        binding.privacyPolicy.movementMethod = LinkMovementMethod.getInstance()

        //アプリ終了時に保存したデータの呼び出し
        val sharedPref = getSharedPreferences(
            "DataStore", Context.MODE_PRIVATE)
//        val int1 = sharedPref.getInt("InputSpinner1", 0)
//        val int2 = sharedPref.getInt("InputSpinner2", 0)
        val str = sharedPref.getString("InputTime","通知時刻を設定")
        val str2 = sharedPref.getString("InputArea","東京")
        val bl = sharedPref.getBoolean("Input", false)

        //初期のボタンの状態
        if (bl){
//            binding.spinner1.setSelection(int1)
//            binding.spinner2.setSelection(int2)
            binding.spinner1.isEnabled = false
            binding.spinner2.isEnabled = false
            binding.selectedTime.text = "$str"
            binding.selectArea.text = "$str2"
            binding.selectTimeBtn.isEnabled = false
            binding.setAlarmBtn.isEnabled = false
            binding.cancelAlarmBtn.isEnabled = true
        }else{
            binding.selectTimeBtn.isEnabled = true
            binding.setAlarmBtn.isEnabled = false
            binding.cancelAlarmBtn.isEnabled = false
        }

        //SelectTimeボタンが押されたら
        binding.selectTimeBtn.setOnClickListener {
            //TimePicker
            showTimePicker()
        }

        //SetAlarmボタンが押されたら
        binding.setAlarmBtn.setOnClickListener {

            //AlarmManager
            setAlarm()
            //ボタンの状態
            binding.spinner1.isEnabled = false
            binding.spinner2.isEnabled = false
            binding.selectTimeBtn.isEnabled = false
            binding.setAlarmBtn.isEnabled = false
            binding.cancelAlarmBtn.isEnabled = true
        }

        //CancelAlarmボタンが押されたら
        binding.cancelAlarmBtn.setOnClickListener {
            cancelAlarm()
            //初期のボタンの状態に戻す
//            binding.spinner1.setSelection(0)
//            binding.spinner2.setSelection(0)
            binding.spinner1.isEnabled = true
            binding.spinner2.isEnabled = true
            binding.selectTimeBtn.isEnabled = true
            binding.setAlarmBtn.isEnabled = false
            binding.cancelAlarmBtn.isEnabled = false
            binding.selectedTime.text = "時刻選択"
            binding.selectArea.text = ""
        }
    }

    //アプリを終了するときのデータの保存
    override fun onPause() {
        super.onPause()
        val sharedPref = getSharedPreferences(
            "DataStore", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
//        editor.putInt("InputSpinner1", binding.spinner1.selectedItemPosition)
//        editor.putInt("InputSpinner2", binding.spinner2.selectedItemPosition)
        editor.putString("InputArea",binding.selectArea.text.toString())
        editor.putString("InputTime", binding.selectedTime.text as String?)
        editor.putBoolean("Input", binding.cancelAlarmBtn.isEnabled)
        editor.apply()
    }

    //スピナー関係
    override fun onItemSelected(
        parent: AdapterView<*>,
        view: View,
        position: Int,
        id: Long,
    ) {
        //spinner1の選択で変わるspinner2(条件分岐)
        if (binding.spinner1.selectedItem == "北海道") {
            selectSpinner(R.array.hokkaido_array)
        } else {
            if (binding.spinner1.selectedItem == "青森県") {
                selectSpinner(R.array.aomori_array)
            } else {
                if (binding.spinner1.selectedItem == "岩手県") {
                    selectSpinner(R.array.iwate_array)
                } else {
                    if (binding.spinner1.selectedItem == "宮城県") {
                        selectSpinner(R.array.miyagi_array)
                    } else {
                        if (binding.spinner1.selectedItem == "秋田県") {
                            selectSpinner(R.array.akita_array)
                        } else {
                            if (binding.spinner1.selectedItem == "山形県") {
                                selectSpinner(R.array.yamagata_array)
                            } else {
                                if (binding.spinner1.selectedItem == "福島県") {
                                    selectSpinner(R.array.fukushima_array)
                                } else {
                                    if (binding.spinner1.selectedItem == "茨城県") {
                                        selectSpinner(R.array.ibaraki_array)
                                    } else {
                                        if (binding.spinner1.selectedItem == "栃木県") {
                                            selectSpinner(R.array.tochigi_array)
                                        } else {
                                            if (binding.spinner1.selectedItem == "群馬県") {
                                                selectSpinner(R.array.gunma_array)
                                            } else {
                                                if (binding.spinner1.selectedItem == "埼玉県") {
                                                    selectSpinner(R.array.saitama_array)
                                                } else {
                                                    if (binding.spinner1.selectedItem == "千葉県") {
                                                        selectSpinner(R.array.chiba_array)
                                                    } else {
                                                        if (binding.spinner1.selectedItem == "東京都") {
                                                            selectSpinner(R.array.tokyo_array)
                                                        } else {
                                                            if (binding.spinner1.selectedItem == "神奈川県") {
                                                                selectSpinner(R.array.kanagawa_array)
                                                            } else {
                                                                if (binding.spinner1.selectedItem == "新潟県") {
                                                                    selectSpinner(R.array.niigata_array)
                                                                } else {
                                                                    if (binding.spinner1.selectedItem == "富山県") {
                                                                        selectSpinner(R.array.toyama_array)
                                                                    } else {
                                                                        if (binding.spinner1.selectedItem == "石川県") {
                                                                            selectSpinner(R.array.ishikawa_array)
                                                                        } else {
                                                                            if (binding.spinner1.selectedItem == "福井県") {
                                                                                selectSpinner(R.array.fukui_array)
                                                                            } else {
                                                                                if (binding.spinner1.selectedItem == "山梨県") {
                                                                                    selectSpinner(R.array.yamanashi_array)
                                                                                } else {
                                                                                    if (binding.spinner1.selectedItem == "長野県") {
                                                                                        selectSpinner(
                                                                                            R.array.nagano_array)
                                                                                    } else {
                                                                                        if (binding.spinner1.selectedItem == "岐阜県") {
                                                                                            selectSpinner(
                                                                                                R.array.gifu_array)
                                                                                        } else {
                                                                                            if (binding.spinner1.selectedItem == "静岡県") {
                                                                                                selectSpinner(
                                                                                                    R.array.shizuoka_array)
                                                                                            } else {
                                                                                                if (binding.spinner1.selectedItem == "愛知県") {
                                                                                                    selectSpinner(
                                                                                                        R.array.aichi_array)
                                                                                                } else {
                                                                                                    if (binding.spinner1.selectedItem == "三重県") {
                                                                                                        selectSpinner(
                                                                                                            R.array.mie_array)
                                                                                                    } else {
                                                                                                        if (binding.spinner1.selectedItem == "滋賀県") {
                                                                                                            selectSpinner(
                                                                                                                R.array.shizuoka_array)
                                                                                                        } else {
                                                                                                            if (binding.spinner1.selectedItem == "京都府") {
                                                                                                                selectSpinner(
                                                                                                                    R.array.kyoto_array)
                                                                                                            } else {
                                                                                                                if (binding.spinner1.selectedItem == "大阪府") {
                                                                                                                    selectSpinner(
                                                                                                                        R.array.osaka_array)
                                                                                                                } else {
                                                                                                                    if (binding.spinner1.selectedItem == "兵庫県") {
                                                                                                                        selectSpinner(
                                                                                                                            R.array.hyogo_array)
                                                                                                                    } else {
                                                                                                                        if (binding.spinner1.selectedItem == "奈良県") {
                                                                                                                            selectSpinner(
                                                                                                                                R.array.nara_array)
                                                                                                                        } else {
                                                                                                                            if (binding.spinner1.selectedItem == "和歌山県") {
                                                                                                                                selectSpinner(
                                                                                                                                    R.array.wakayama_array)
                                                                                                                            } else {
                                                                                                                                if (binding.spinner1.selectedItem == "鳥取県") {
                                                                                                                                    selectSpinner(
                                                                                                                                        R.array.tottori_array)
                                                                                                                                } else {
                                                                                                                                    if (binding.spinner1.selectedItem == "島根県") {
                                                                                                                                        selectSpinner(
                                                                                                                                            R.array.shimane_array)
                                                                                                                                    } else {
                                                                                                                                        if (binding.spinner1.selectedItem == "岡山県") {
                                                                                                                                            selectSpinner(
                                                                                                                                                R.array.okayama_array)
                                                                                                                                        } else {
                                                                                                                                            if (binding.spinner1.selectedItem == "広島県") {
                                                                                                                                                selectSpinner(
                                                                                                                                                    R.array.hiroshima_array)
                                                                                                                                            } else {
                                                                                                                                                if (binding.spinner1.selectedItem == "山口県") {
                                                                                                                                                    selectSpinner(
                                                                                                                                                        R.array.yamaguchi_array)
                                                                                                                                                } else {
                                                                                                                                                    if (binding.spinner1.selectedItem == "徳島県") {
                                                                                                                                                        selectSpinner(
                                                                                                                                                            R.array.tokushima_array)
                                                                                                                                                    } else {
                                                                                                                                                        if (binding.spinner1.selectedItem == "香川県") {
                                                                                                                                                            selectSpinner(
                                                                                                                                                                R.array.kagawa_array)
                                                                                                                                                        } else {
                                                                                                                                                            if (binding.spinner1.selectedItem == "愛媛県") {
                                                                                                                                                                selectSpinner(
                                                                                                                                                                    R.array.ehime_array)
                                                                                                                                                            } else {
                                                                                                                                                                if (binding.spinner1.selectedItem == "高知県") {
                                                                                                                                                                    selectSpinner(
                                                                                                                                                                        R.array.kochi_array)
                                                                                                                                                                } else {
                                                                                                                                                                    if (binding.spinner1.selectedItem == "福岡県") {
                                                                                                                                                                        selectSpinner(
                                                                                                                                                                            R.array.fukuoka_array)
                                                                                                                                                                    } else {
                                                                                                                                                                        if (binding.spinner1.selectedItem == "佐賀県") {
                                                                                                                                                                            selectSpinner(
                                                                                                                                                                                R.array.saga_array)
                                                                                                                                                                        } else {
                                                                                                                                                                            if (binding.spinner1.selectedItem == "長崎県") {
                                                                                                                                                                                selectSpinner(
                                                                                                                                                                                    R.array.nagasaki_array)
                                                                                                                                                                            } else {
                                                                                                                                                                                if (binding.spinner1.selectedItem == "熊本県") {
                                                                                                                                                                                    selectSpinner(
                                                                                                                                                                                        R.array.kumamoto_array)
                                                                                                                                                                                } else {
                                                                                                                                                                                    if (binding.spinner1.selectedItem == "大分県") {
                                                                                                                                                                                        selectSpinner(
                                                                                                                                                                                            R.array.oita_array)
                                                                                                                                                                                    } else {
                                                                                                                                                                                        if (binding.spinner1.selectedItem == "宮崎県") {
                                                                                                                                                                                            selectSpinner(
                                                                                                                                                                                                R.array.miyazaki_array)
                                                                                                                                                                                        } else {
                                                                                                                                                                                            if (binding.spinner1.selectedItem == "鹿児島県") {
                                                                                                                                                                                                selectSpinner(
                                                                                                                                                                                                    R.array.kagoshima_array)
                                                                                                                                                                                            } else {
                                                                                                                                                                                                selectSpinner(
                                                                                                                                                                                                    R.array.okinawa_array)
                                                                                                                                                                                            }
                                                                                                                                                                                        }
                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //spinner2で選んだ値を取得
        // リスナーを登録
        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //　アイテムが選択された時
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long,
            ) {
                val spinnerParent = parent as Spinner
                val code = spinnerParent.selectedItem as String//spinner2で選んだ値を変数codeに入れた。
                key = code
            }
            //　アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //セレクトボックスが選ばれなかったときの処理
    }

    //関数でまとめる
    private fun selectSpinner(bbb:Int) {
        val adapter2 = ArrayAdapter.createFromResource(
            this,
            bbb, android.R.layout.simple_spinner_dropdown_item
        )
        binding.spinner2.adapter = adapter2
    }
    //スピナー関係ここまで

    //↓ここから関数
    //Android 8.0 以上では、通知を送信する前に通知チャネルを作成する必要がある。
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "TextChannel"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("TEST_CHANNEL_ID", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    //TimePicker
    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        picker.show(supportFragmentManager, "TEST_CHANNEL_ID")

        picker.addOnPositiveButtonClickListener {
            if (picker.hour >= 12) {
                val pm = "PM " + String.format("%02d", picker.hour - 12) + ":" + String.format(
                    "%02d", picker.minute
                )
                binding.selectedTime.text = pm
            } else {
                val am = "AM " + String.format("%02d", picker.hour) + ":" + String.format(
                    "%02d", picker.minute
                )
                binding.selectedTime.text = am
            }
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            if (calendar.timeInMillis < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            //ボタンの状態
            binding.selectTimeBtn.isEnabled = true
            binding.setAlarmBtn.isEnabled = true
            binding.cancelAlarmBtn.isEnabled = false
        }
    }

    //AlarmManager
    private fun setAlarm() {
        //spinner2で選んだ値をAlarmReceiver.ktへ渡す
        val key2 = key
        binding.selectArea.text = key2
        val address = mapOf("稚内" to "011000","旭川" to "012010","留萌" to "012020","網走" to "013010","北見" to "013020","紋別" to "013030","根室" to "014010","釧路" to "014020",
            "帯広" to "014030","室蘭" to "015010","浦河" to "015020","札幌" to "016010","岩見沢" to "016020","倶知安" to "016030","函館" to "017010","江差" to "017020",
            "青森" to "020010","むつ" to "020020","八戸" to "020030","盛岡" to "030010","宮古" to "030020","大船渡" to "030030","仙台" to "040010","白石" to "040020","秋田" to "050010",
            "横手" to "050020","山形" to "060010","米沢" to "060020","酒田" to "060030","新庄" to "060040","福島" to "070010","小名浜" to "070020","若松" to "070030","水戸" to "080010",
            "土浦" to "080020","宇都宮" to "090010","大田原" to "090020","前橋" to "100010","みなかみ" to "100020","さいたま" to "110010","熊谷" to "110020","秩父" to "110030","千葉" to "120010",
            "銚子" to "120020","東京" to "130010","大島" to "130020","八丈島" to "130030","父島" to "130040","横浜" to "140010","小田原" to "140020","新潟" to "150010","長岡" to "150020",
            "高田" to "150030","相川" to "150040","富山" to "160010","伏木" to "160020","金沢" to "170010","輪島" to "170020","福井" to "180010","敦賀" to "180020","甲府" to "190010",
            "河口湖" to "190020","長野" to "200010","松本" to "200020","飯田" to "200030","岐阜" to "210010","高山" to "210020","静岡" to "220010","網代" to "220020","三島" to "220030",
            "浜松" to "220040","名古屋" to "230010","豊橋" to "230020","津" to "240010","尾鷲" to "240020","大津" to "250010","彦根" to "250020","京都" to "260010","舞鶴" to "260020",
            "大阪" to "270000","神戸" to "280010","豊岡" to "280020","奈良" to "290010","風屋" to "290020","和歌山" to "300010","潮岬" to "300020","鳥取" to "310010","米子" to "310020",
            "松江" to "320010","浜田" to "320020","西郷" to "320030","岡山" to "330010","津山" to "330020","広島" to "340010","庄原" to "340020","下関" to "350010","山口" to "350020",
            "柳井" to "350030","萩" to "350040","徳島" to "360010","日和佐" to "360020","高松" to "370000","松山" to "380010","新居浜" to "380020","宇和島" to "380030","高知" to "390010",
            "室戸岬" to "390020","清水" to "390030","福岡" to "400010","八幡" to "400020","飯塚" to "400030","久留米" to "400040","佐賀" to "410010","伊万里" to "410020","長崎" to "420010",
            "佐世保" to "420020","厳原" to "420030","福江" to "420040","熊本" to "430010","阿蘇乙姫" to "430020","牛深" to "430030","人吉" to "430040","大分" to "440010","中津" to "440020",
            "日田" to "440030","佐伯" to "440040","宮崎" to "450010","延岡" to "450020","都城" to "450030","高千穂" to "450040","鹿児島" to "460010","鹿屋" to "460020","種子島" to "460030",
            "名瀬" to "460040","那覇" to "471010","名護" to "471020","久米島" to "471030","南大東" to "472000","宮古島" to "473000","石垣島" to "474010","与那国島" to "474020")
        val key3 = address[key2].toString()//spinner2で選んだ値に紐づくコードを変数key3にいれた。
        val sharedPref = getSharedPreferences(
            "DataStore2", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("InputCode", key3)
        editor.apply()

        //AlarmManager
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        // Broadcast にメッセージを送るための設定
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        //繰り返しの設定(ただし、setRepeatingではDozeモードで発動しない)
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
//            AlarmManager.INTERVAL_DAY, pendingIntent//1日間隔でアラーム
//        )
        //DozeモードでもAlarm発動する(ただし1回のみ)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            pendingIntent
        )

        //トーストを表示
        Toast.makeText(this, "設定完了", Toast.LENGTH_SHORT).show()
    }

    //設定したアラームを解除
    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        // Broadcast にメッセージを送るための設定
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
        //トースト表示
        Toast.makeText(this, "設定解除", Toast.LENGTH_LONG).show()
    }
}