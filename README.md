## Androidアプリ「傘いる？いらない？」
<img src="https://user-images.githubusercontent.com/115522917/203840479-88293bc7-c54f-43b4-9b9d-8bcab9bb92f0.jpg" width="75%">

## URL
https://play.google.com/store/apps/details?id=io.github.izm_o.umbrellaapp

## 概要
- 2022/11/8 アプリリリース(Google Playで公開)
- 地域と通知時刻を選んで設定すると、毎日その日の降水確率をプッシュ通知でお知らせしてくれます。<br>
  傘が必要かどうかを判断する為だけの機能に絞ったシンプルなアプリです。

## 技術環境
- 開発言語：Kotlin
- 開発環境：Android Studio
- 使用技術：Kotlin Coroutine , ViewBinding , ConstraintLayout
- バージョン管理：Git , GitHub
- デザインツール：Adobe Illustrator , Adobe Photoshop

## 今後追加予定の機能など
- ターゲットSDK33への対応（uses-permissionにPOST_NOTIFICATIONSを追加 & 通知許可アラートの実装）
- 今日の降水確率を通知するか、明日の降水確率を通知するか選択できる機能を追加予定

## アプリの制作について
### 制作期間
20日間
### なぜこのアプリを作ったのか
私自身が一番欲しいアプリを作りました。<br>
天気予報を見る習慣がなく出かける時に雨が降っていなければ傘は持たないという生き方をしてきましたが、<br>
やはり濡れる事も多く、何度も天気予報をみる習慣をつけようと思っては挫折した経験からこのアプリを作りました。
### こだわった点
- 天気予報を見る習慣のない人でも毎日チェックするように、余計な情報は載せず、傘が必要かどうか判断する為だけのシンプルな機能にした点。
- 位置情報の取得に抵抗のあるユーザーにも使ってもらえるように、位置情報は使わずプルダウンで地域を設定するようにした点。
### アプリの制作・リリースをしてみて
独学でkotlinを勉強し初めてのアプリ制作だったので自力で最後まで完成できるか不安もあったが、
エラーを何度も繰り返し、そのたびに調べ、考え、問題を解決してきた事が今はすごく自分の力になったと感じています。

## 謝辞
このアプリでは<a href="https://weather.tsukumijima.net/">天気予報 API（livedoor 天気互換）</a>を使用させていただいております。<br>
制作者の<a href="https://github.com/tsukumijima">tsukumi</a>様には、この場をお借りして感謝申し上げます。
