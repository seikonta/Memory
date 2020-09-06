package app.takasu.kon.memory

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val customList: List<String?>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    //val items: MutableList<ImageData> = mutableListOf()
    lateinit var listener: OnItemClickListener

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val drawImage: ImageView = view.findViewById(R.id.drawing)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_image_data_cell, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return customList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // val item = items[position]
        var imageID = Uri.parse(customList[position]?.albumImage.toString())
        holder.drawImage.setImageURI(imageID)
        holder.view.setOnClickListener {
            listener.onItemClickListener(it, position, customList[position])
        }
    }

    interface OnItemClickListener {
        fun onItemClickListener(view: View, position: Int, clickedText: String?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}