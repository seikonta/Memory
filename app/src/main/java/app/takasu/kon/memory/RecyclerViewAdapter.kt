package app.takasu.kon.memory

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_image_data_cell.view.*

class RecyclerViewAdapter(
    private val memoList: OrderedRealmCollection<Memo>,
    private val listener: OnItemClickListener,
    private val autoUpdate: Boolean
    ) :
    RealmRecyclerViewAdapter<Memo, RecyclerViewAdapter.ViewHolder>(memoList, autoUpdate) {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
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
//        val item = items[position]
//        if (customList[position]?.albumImage != null) {
//            if (Uri.parse(customList[position]?.albumImage) != null) {
//                var imageID = Uri.parse(customList[position]?.albumImage)
//                holder.drawImage.setImageURI(imageID)
//                holder.view.setOnClickListener {
//                    listener.onItemClickListener(it, position, customList[position].toString())
//                }
//            }
//        }

        val memo: Memo = memoList?.get(position) ?: return

        holder.container.setOnClickListener {
            listener.onItemClick(memo)
        }

        holder.container.setOnLongClickListener {
            listener.onItemLongClick(memo)
            true
        }
        holder.drawImage.setImageURI(Uri.parse(memo.imageUriString))
    }

    interface OnItemClickListener {
        fun onItemClick(item: Memo)
        fun onItemLongClick(item: Memo)
    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        this.listener = listener
//    }
}