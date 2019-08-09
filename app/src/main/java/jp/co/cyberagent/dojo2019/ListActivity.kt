package jp.co.cyberagent.dojo2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

       // val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ViewAdapter( makeList(), this, this)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        scanButton.setOnClickListener{
            IntentIntegrator(this).initiateScan()
        }
    }

    private fun makeList() : List<Profile> {

        //永続データベースを作成
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").build()

        //dbからデータを取得
        val dao = db.profileDao()

        //データリスト
        val dataList = mutableListOf<Profile>()

        dao.getAll().observe(this, Observer <List<Profile>> {profiles ->
            if(profiles != null){
                for ( i in profiles){
                    val data: Profile = Profile().also{
                        it.name = i.name
                        it.github = i.github
                        it.twitter= i.twitter
                    }
                    dataList.add(data)
                }
            }
            //二回目以降のデータ更新を通知
            recyclerView.adapter?.notifyDataSetChanged()
        })

        return dataList
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()


                val result1 = result.contents.toString()


                val intent = Intent(this, GetActivity::class.java)
                intent.putExtra("RESULT", result1)

                startActivity(intent)


            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        }

}
