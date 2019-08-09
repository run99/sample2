package jp.co.cyberagent.dojo2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.room.Room
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class GetActivity : AppCompatActivity() {

    var index : Int? = null
    var index2 : Int? = null
    var index3: Int? = null
    var index4 : Int? = null

    var getName : String? = null
    var getTwitter : String? = null
    var getGithub : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get)

        //var result1 = "jalsjlj?kalj&twjlajljl&gh"
        val dataResult = intent.getStringExtra("RESULT")
        val dataText = findViewById<TextView>(R.id.contentText)
        dataText.setText(dataResult)


        index = dataResult.indexOf("=")
        index2 = dataResult.indexOf("&")
        index3 = dataResult.indexOf("tw")
        index4 = dataResult.indexOf("&gh")

        getName = dataResult.substring(index!! + 1, index2!!)
        getTwitter = dataResult.substring(index3!! + 3, index4!!)
        getGithub = dataResult.substring(index4!! + 4)

        val nameText = findViewById<TextView>(R.id.nameText)
        nameText.setText(getName)

        val githubText = findViewById<TextView>(R.id.githubText)
        githubText.setText(getGithub)

        val twitterText = findViewById<TextView>(R.id.twitterText)
        twitterText.setText(getTwitter)


    }

    fun add(view: View) {

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").build()

        val profile = Profile()
        profile.uid = Random().nextInt()
        profile.name = getName
        profile.github = getGithub
        profile.twitter = getTwitter

        thread {
            db.profileDao().insert(profile)
        }

        finish()

    }

   /* private fun check(name: String?, github: String?, twitter: String?) {

        //Profileクラスのインスタンスを生成
        val profile = Profile()

        //Profileのそれぞれの要素に、Addの要素を代入する
        profile.name  = name
        profile.github = github
        profile.twitter = twitter

        //Logで確認する
        Log.d("Proflie", profile.name)
        Log.d("Profile", profile.github)
        Log.d("Profile", profile.twitter)

    }*/
}
