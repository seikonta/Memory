package app.takasu.kon.memory

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val readRequestCode: Int = 42

    val realm: Realm = Realm.getDefaultInstance()

    val  imageData: List<ImageData> = listOf(
        ImageData(R.drawable.image0),
        ImageData(R.drawable.image1),
        ImageData(R.drawable.image2),
        ImageData(R.drawable.image3),
        ImageData(R.drawable.image4),
        ImageData(R.drawable.image5)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val preview = Intent(this, PreviewActivity::class.java)

        val memo: Memo? = read()

        if (memo != null) {
            // RecyclerViewの画像をmemo.imageUriStringにする
        }

        var mainText = intent.getStringExtra("main")

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
            override fun onItemClickListener(view: View, position: Int, clickedText: ImageData) {
                if (clickedText == ImageData(R.drawable.image0)) {
                    preview.putExtra("image", R.drawable.image0)
                    preview.putExtra("title", "桜木町周辺")
                    preview.putExtra("main", mainText)
                    startActivity(preview)
                }
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

    fun save(tag: Int, imageUriString: String, title: String, content: String) {
        val memo: Memo? = read()

        realm.executeTransaction {
            if (memo != null) {
                equal(memo, tag, imageUriString, title, content)
            } else {
                val newMemo: Memo = it.createObject(Memo::class.java)
                equal(newMemo, tag, imageUriString, title, content)
            }
        }
    }

    fun equal(memo: Memo, tag: Int, imageUriString: String, title: String, content: String) {
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
                if (memo?.tag != null) {
                    tag = memo.tag!! + 1
                }
                else {
                    tag = 0
                }
                var title: String = ""
                var content: String = ""
                save(tag, uri.toString(), title, content)
            }
        }
    }

}