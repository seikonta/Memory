package app.takasu.kon.memory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        val main = Intent(this, MainActivity::class.java)

        var imageId = intent.getIntExtra("image", 0)
        var titleText = intent.getStringExtra("title")
        var mainText = intent.getStringExtra("main")

        previewImage.setImageResource(imageId)
        titleEditText.setText(titleText)
        mainEditText.setText(mainText)

        backButton.setOnClickListener {
            main.putExtra("main", mainText)
            finish()
        }

        saveButton.setOnClickListener {
            mainText = mainEditText.text.toString()
        }

    }
}