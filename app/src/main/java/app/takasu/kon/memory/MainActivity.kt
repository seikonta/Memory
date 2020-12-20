package app.takasu.kon.memory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_preview.*
import kotlinx.android.synthetic.main.activity_preview.view.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val readRequestCode: Int = 42

    private var title: String = ""
    val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    val memo: RealmResults<Memo> = readAll()

    val adapter = RecyclerViewAdapter(memo, object : RecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(item: Memo) {
            val preview = Intent(this@MainActivity, PreviewActivity::class.java)
            preview.putExtra("tag", item.id)
            startActivity(preview)
        }
    }, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        if (memo != null) {
//            // RecyclerViewの画像をmemo.imageUriStringにする
//        }

        //var titleText = intent.getStringExtra("title")
        //var mainText = intent.getStringExtra("main")


        val layoutManager = LinearLayoutManager(this)

        //val recyclerAdapter = Intent(this, RecyclerViewAdapter::class.java).apply {}

        imageRecycler.layoutManager = layoutManager
        imageRecycler.adapter = adapter
        imageRecycler.setHasFixedSize(true)

        galleryButton.setOnClickListener {
            println("clicked galleryButton")
            val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, readRequestCode)
            //startActivity(recyclerAdapter)
        }

        /*darkThemeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }*/

    }

    override fun onDestroy() {
        super.onDestroy()

        realm.close()
    }

    fun readAll() : RealmResults<Memo> {
        return realm.where(Memo::class.java).findAll()
    }

    fun save(tag: String, imageUriString: String?, title: String, content: String) {
        realm.executeTransaction {
            val memo: Memo = it.createObject(Memo::class.java, UUID.randomUUID())

            if (memo != null) {
                equal(memo, tag, imageUriString, title, content)
                realm.copyToRealm(memo)
            } else {
                val newMemo: Memo = it.createObject(Memo::class.java)
                equal(newMemo, tag, imageUriString, title, content)
                realm.copyToRealm(newMemo)
            }
        }
    }

    fun equal(memo: Memo, id: String, imageUriString: String?, title: String, content: String) {
        memo.id = id
        memo.imageUriString = imageUriString
        memo.title = title
        memo.content = content
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == readRequestCode && resultCode == Activity.RESULT_OK) {
            data?.data.also { uri ->
                var tag: String = UUID.randomUUID().toString()
                /*tag = if (memo?.tag != null) {
                    memo.tag!! + 1
                } else {
                    0
                }*/
                var imageUri: String = uri.toString()

                var title: String = ""

                var content: String = ""

                save(tag, imageUri, title, content)
            }
        }
    }

}