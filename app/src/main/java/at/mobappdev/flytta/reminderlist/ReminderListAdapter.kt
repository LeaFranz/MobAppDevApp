package at.mobappdev.flytta.reminderlist

import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.mobappdev.flytta.R
import at.mobappdev.flytta.alarm.Alarms
import at.mobappdev.flytta.remindersettings.ReminderPrefs
import kotlinx.android.synthetic.main.item.view.*
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReminderListAdapter(
    private val list: MutableList<ReminderListItem> = mutableListOf(),
    private var clickListener: OnItemClickListener
) : RecyclerView.Adapter<ReminderListAdapter.ViewHolder>() {

    private var rowIndex: Int = -1 //no reminder is active, usually index of activated reminder
    private lateinit var viewHolder: ViewHolder

    //click listener for items
    interface OnItemClickListener {
        fun onItemClicked(item: ReminderListItem, position: Int)
    }

    /**
     * reminder gets deleted via context menu
     * index: index of to be removed item
     */
    private fun deleteItem(index: Int) {
        if (rowIndex == index) { //active reminder
            rowIndex = -1 //no reminder activated
            Alarms.removeDailyAlarm(viewHolder.itemView.context)
            Alarms.removeIntervalAlarm(viewHolder.itemView.context)
        } else if(isActiveSwitchAfterIndex(index)) {
            rowIndex -= 1 //changes rowIndex to new index of activated reminder
        }

        list.removeAt(index)
        notifyChange() //update view
    }

    /**
     * checks is currently activated switch is after the deleted switch
     * eg. activated (index 5), deleted reminder (index 3)
     */
    private fun isActiveSwitchAfterIndex(currentIndex:Int):Boolean{
        for((index, _) in list.withIndex()){
            if(ReminderPrefs.getSwitchActive(list[index].reminderID, viewHolder.itemView.context)){
                if(currentIndex < index){
                    return true
                }
            }
        }
        return false
    }

    /**
     * creates viewholder
     * views are created
     * https://medium.com/androiddevelopers/android-data-binding-recyclerview-db7c40d9f0e4
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item,
            parent, false
        )
        return ViewHolder(
            itemView
        )
    }

    /**
     * binds viewholder
     * specific data is assigned to views
     * called for every item
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val switch = holder.itemView.findViewById<Switch>(R.id.remSwitch)
        viewHolder = holder

        holder.initialize(list[position], clickListener)

        if (rowIndex == -1) { //no switch currently active
            switch.isChecked =
                ReminderPrefs.getSwitchActive(list[position].reminderID, holder.itemView.context)
            if (switch.isChecked) {
                rowIndex = position
            }
        } else {
            // there is an active switch
            switch.isChecked = (position == rowIndex) //check if current switch is the checked one
            //unchecks a switch if another one gets checked
            ReminderPrefs.setSwitchActive(
                (position == rowIndex),
                list[position].reminderID,
                holder.itemView.context
            )
        }

        //listening to click on switch
        switch.setOnClickListener {
            if (position == rowIndex) {
                //deactivate reminder
                switch.isChecked = false
                ReminderPrefs.setSwitchActive(
                    false,
                    list[position].reminderID,
                    holder.itemView.context
                )
                rowIndex = -1 //no reminder active
                Alarms.removeDailyAlarm(holder.itemView.context)
                Alarms.removeIntervalAlarm(holder.itemView.context)
            } else {
                //activate reminder
                Alarms.setDailyAlarm(holder.itemView.context, list[position])
                rowIndex = position
                notifyChange() //notify other reminders - calls onBindViewHolder
            }
        }
    }

    //for notifying other reminders (only one is allowed to be active)
    private var handler: Handler = Handler()
    private fun notifyChange() {
        try {
            notifyDataSetChanged()
        } catch (ex: Exception) {
            handler.post { notifyDataSetChanged() }
        }
    }

    override fun getItemCount() = list.size

    /**
     * Viewholder class
     * the actual item view
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private val reminderName: TextView = itemView.reminderName
        private val interval: TextView = itemView.interval
        private val timeStart: TextView = itemView.timeStart
        private val timeEnd: TextView = itemView.timeEnd
        private val days: TextView = itemView.days
        private lateinit var auth: FirebaseAuth
        private lateinit var currentReminderId: String

        fun initialize(item: ReminderListItem, listener: OnItemClickListener) {
            auth = FirebaseAuth.getInstance()
            reminderName.text = item.reminderName
            interval.text = item.interval
            timeStart.text = item.timeStart
            timeEnd.text = item.timeEnd
            currentReminderId = item.reminderID

            var dayText = ""
            var counter = 0
            for (day in item.days) { //concat of days
                counter++
                dayText += if (counter == item.days.size) {
                    day.toString().toLowerCase(Locale.ROOT).capitalize()
                } else {
                    day.toString().toLowerCase(Locale.ROOT).capitalize() + ", "
                }

            }
            days.text = dayText

            itemView.setOnCreateContextMenuListener(this) //context menu for deleting item
            itemView.setOnClickListener {
                listener.onItemClicked(item, adapterPosition)
            }
        }

        /**
         * creating context menu
         */
        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val item = menu?.add(0, 1, 0, "Delete reminder")
            item?.setOnMenuItemClickListener(this)
        }

        /**
         * Firestore Firebase
         * deleting reminder from db
         * listener for context menu click
         */
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            val db = Firebase.firestore
            val userId = auth.uid

            if (userId != null) {
                db.collection("users").document(userId).collection("reminder")
                    .document(currentReminderId)
                    .delete()
                    .addOnSuccessListener {
                        Log.d(
                            "reminderlistadapter",
                            "Reminder successfully deleted. "
                        )
                        deleteItem(adapterPosition) //deleting item from list
                    }
                    .addOnFailureListener { e ->
                        Log.w(
                            "reminderlistadapter",
                            "Error deleting reminder ",
                            e
                        )
                        deleteItem(adapterPosition)
                    }
            }
            return true
        }
    }
}