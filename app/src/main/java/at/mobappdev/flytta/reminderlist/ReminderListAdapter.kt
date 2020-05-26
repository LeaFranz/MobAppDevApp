package at.mobappdev.flytta.reminderlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.mobappdev.flytta.R
import kotlinx.android.synthetic.main.item.view.*

class ReminderListAdapter(private val list: List<ReminderListItem>, private var clickListener: OnItemClickListener) : RecyclerView.Adapter<ReminderListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(item: ReminderListItem, position:Int)
    }

    // represents one line
    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        private val reminderName: TextView = itemView.reminderName
        private val interval: TextView = itemView.interval
        private val intervalType: TextView = itemView.intervalType
        private val dayStart: TextView = itemView.dayStart
        private val dayEnd: TextView = itemView.dayEnd
        private val timeStart: TextView = itemView.timeStart
        private val timeEnd: TextView = itemView.timeEnd

        fun initialize(item: ReminderListItem, listener: OnItemClickListener){
            reminderName.text = item.reminderName
            interval.text = item.interval
            intervalType.text = item.intervalType
            dayStart.text = item.dayStart
            dayEnd.text = item.dayEnd
            timeStart.text = item.timeStart
            timeEnd.text = item.timeEnd

            //listener for clicking on listitems
            itemView.setOnClickListener{
                listener.onItemClicked(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // not called often
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item,
            parent, false)
        return ViewHolder(
            itemView
        )
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