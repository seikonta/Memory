package app.takasu.kon.memory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_preview.*
import kotlinx.android.synthetic.main.activity_preview.view.*

class MainActivity : AppCompatActivity() {

    private val readRequestCode: Int = 42



    private var title: String = ""
    val realm: Realm = Realm.getDefaultInstance()

    val memo: Memo? = read()

    val  imageData: List<ImageData> = listOf(
        ImageData(memo?.imageUriString)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val preview = Intent(this, PreviewActivity::class.java)

        if (memo != null) {
            // RecyclerViewの画像をmemo.imageUriStringにする
        }

        //var titleText = intent.getStringExtra("title")
        //var mainText = intent.getStringExtra("main")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = RecyclerViewAdapter(imageData)
        val layoutManager = LinearLayoutManager(this)

        imageRecycler.layoutManager = layoutManager
        imageRecycler.adapter = adapter
        imageRecycler.setHasFixedSize(true)

        galleryButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, readRequestCode)
        }

        adapter.setOnItemClickListener(object : RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClickListener(view: View, position: Int, clickedText: String?) {
                preview.putExtra("tag", memo?.tag)
                preview.putExtra("image", memo?.imageUriString.toString())
                preview.putExtra("title", memo?.title)
                preview.putExtra("main", memo?.content)
                startActivity(preview)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        realm.close()
    }

    fun read() : Memo? {
        return realm.where(Memo::class.java).findFirst()
    }

    fun save(tag: Int, imageUriString: String?, title: String, content: String) {
        val memo: Memo? = read()

        realm.executeTransaction {
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

    fun equal(memo: Memo, tag: Int, imageUriString: String?, title: String, content: String) {
        memo.tag = tag
        memo.imageUriString = imageUriString
        memo.title = title
        memo.content = content
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == readRequestCode && resultCode == Activity.RESULT_OK) {
            data?.data.also { uri ->
                val memo: Memo? = read()
                var tag: Int
                tag = if (memo?.tag != null) {
                    memo.tag!! + 1
                } else {
                    0
                }
                var imageUri: String = uri.toString()

                var content: String = ""
                save(tag, imageUri, title, content)
            }
        }
    }

}