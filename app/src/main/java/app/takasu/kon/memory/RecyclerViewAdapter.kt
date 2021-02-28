package app.takasu.kon.memory

import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_image_data_cell.view.*
import java.io.File
import java.io.IOException

class RecyclerViewAdapter(
    private val memoList: OrderedRealmCollection<Memo>,
    private val listener: OnItemClickListener,
    autoUpdate: Boolean
    ) :
    RealmRecyclerViewAdapter<Memo, RecyclerViewAdapter.ViewHolder>(memoList, autoUpdate) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: ConstraintLayout = view.container
        val drawImage: ImageView = view.findViewById(R.id.drawing)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_image_data_cell, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val memo: Memo = memoList?.get(position) ?: return

        holder.container.setOnClickListener {
            listener.onItemClick(memo)
        }

        holder.container.setOnLongClickListener {
            listener.onItemLongClick(memo)
            true
        }

        try {
            var imageUri = Uri.parse(memo.imageUriString)
            var bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri)
            holder.drawImage.setImageURI(Uri.parse(memo.imageUriString))
        }

        catch (e: IOException) {
            e.printStackTrace()
        }

    }

    interface OnItemClickListener {
        fun onItemClick(item: Memo)
        fun onItemLongClick(item: Memo)
    }

}