package at.mobappdev.flytta

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*

class Adapter(private val list: List<Item>, private var clickListener: OnItemClickListener) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(item:Item, position:Int)
    }

    // represents one line
    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        private val textView1: TextView = itemView.text_view1
        private val textView2: TextView = itemView.text_view2

        fun initialize(item:Item, listener:OnItemClickListener){
            textView1.text = item.text1
            textView2.text = item.text2

            itemView.setOnClickListener{
                listener.onItemClicked(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // not called often
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item,
            parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //called very often
//        val currentItem = list[position] 
//        holder.textView1.text = currentItem.text1
//        holder.textView2.text = currentItem.text2
        holder.initialize(list[0], clickListener)
    }

    override fun getItemCount() = list.size
}