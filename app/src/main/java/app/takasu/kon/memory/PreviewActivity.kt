package app.takasu.kon.memory

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_preview.*
import java.text.SimpleDateFormat
import java.util.*

class PreviewActivity : AppCompatActivity() {
    val realm: Realm = Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        val main = Intent(this, MainActivity::class.java)

        var tagString = intent.getStringExtra("tag")

//        var imageId = intent.getStringExtra("image")
//        var titleText = intent.getStringExtra("title")
//        var mainText = intent.getStringExtra("main")

        var contents = realm.where(Memo::class.java).equalTo("id", tagString).findFirst()!!
        previewImage.setImageURI(Uri.parse(contents.imageUriString))
        titleEditText.setText(contents.title)
        mainEditText.setText(contents.content)
        dateText.text = SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN).format(contents.createdAt)

        backButton.setOnClickListener {
            //main.putExtra("title", titleText)
            //main.putExtra("main", mainText)
//            val memo: Memo? = read(tagString)
//            if (memo?.title != titleEditText.text.toString() || memo?.content != mainEditText.text.toString()) {
//                AlertDialog.Builder(this)
//                    .setTitle("注意")
//                    .setMessage("変更内容が保存されていません。変更を破棄してよろしいですか？")
//                    .setPositiveButton("変更内容を破棄") { _, _ -> finish() }
//                    .setNegativeButton("キャンセル") { _, _ ->
//
//                    }
//                    .show()
//            }
//            else {
//                finish()
//            }
            finish()
        }

//        saveButton.setOnClickListener {
//            save(tagString,/* imageId, */titleEditText.text.toString(), mainEditText.text.toString())
//        }

    }

    override fun onDestroy() {
        super.onDestroy()

        var tagString = intent.getStringExtra("tag")
        save(tagString,/* imageId, */titleEditText.text.toString(), mainEditText.text.toString())
    }

    fun read(tag: String) : Memo? {
        return realm.where(Memo::class.java).equalTo("id", tag).findFirst()
    }

    fun save(tag: String,/* imageUriString: String?, */title: String, content: String) {
        val memo: Memo? = read(tag)

        realm.executeTransaction {
            if (memo != null) {
                equal(memo, /*tag, imageUriString, */title, content)
                realm.copyToRealm(memo)
            } else {
                val newMemo: Memo = it.createObject(Memo::class.java)
                equal(newMemo, /*tag, imageUriString, */title, content)
                realm.copyToRealm(newMemo)
            }
        }

//        realm.executeTransaction {
//            val memo: Memo? = read()
//            equal(memo!!, title, content)
//        }

        //Toast.makeText(applicationContext, "保存しました", Toast.LENGTH_SHORT).show()
    }

    fun equal(memo: Memo, /*tag: String, imageUriString: String?, */title: String, content: String) {
//        memo.id = tag
//        memo.imageUriString = imageUriString
        memo.title = title
        memo.content = content
    }

}