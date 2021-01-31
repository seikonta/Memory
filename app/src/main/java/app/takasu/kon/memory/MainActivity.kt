package app.takasu.kon.memory

import android.app.Activity
import android.content.Intent
import android.media.MediaRouter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_preview.*
import kotlinx.android.synthetic.main.activity_preview.view.*
import java.text.SimpleDateFormat
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

        override fun onItemLongClick(item: Memo) {
            delete(item)
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


//        val adapter = RecyclerViewAdapter(this, memo, object : RecyclerViewAdapter.OnItemClickListener {
//            override fun onItemClick(item: Memo) {
//                println("clicked galleryButton")
//                val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
//                galleryIntent.type = "image/*"
//                startActivityForResult(galleryIntent, readRequestCode)
//                //startActivity(recyclerAdapter)
//            }
//        }, true)

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
        return realm.where(Memo::class.java).findAll().sort("createdAt", Sort.ASCENDING)
    }

    fun save(imageUriString: String?, title: String, content: String, date: Date) {
        realm.executeTransaction {
//            val memo: Memo = it.createObject(Memo::class.java, UUID.randomUUID())
//
//            if (memo != null) {
//                equal(memo, tag, imageUriString, title, content)
//                realm.copyToRealm(memo)
//            } else {
//                val newMemo: Memo = it.createObject(Memo::class.java)
//                equal(newMemo, tag, imageUriString, title, content)
//                realm.copyToRealm(newMemo)
//            }

            val memo: Memo = it.createObject(Memo::class.java, UUID.randomUUID().toString())
            equal(memo, imageUriString, title, content, date)
        }
    }

    fun equal(memo: Memo, imageUriString: String?, title: String, content: String, date: Date) {
        memo.imageUriString = imageUriString
        memo.title = title
        memo.content = content
        memo.createdAt = date
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == readRequestCode && resultCode == Activity.RESULT_OK) {
            data?.data.also { uri ->
                //var tag: String = UUID.randomUUID().toString()
                /*tag = if (memo?.tag != null) {
                    memo.tag!! + 1
                } else {
                    0
                }*/
                var imageUri: String = uri.toString()

                var title: String = ""

                var content: String = ""

                //val memo : Memo = memoList?.get(position) ?: return
                var date: Date = Date(System.currentTimeMillis())

                save(imageUri, title, content, date)
            }
        }
    }

    fun delete(memo: Memo) {
        realm.executeTransaction {
            //val memo = realm.where(Memo::class.java).equalTo("id", id).findFirst()?: return@executeTransaction
            memo.deleteFromRealm()
        }
    }

}