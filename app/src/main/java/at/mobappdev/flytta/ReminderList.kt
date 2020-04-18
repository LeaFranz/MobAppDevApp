package at.mobappdev.flytta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_reminder_list.*

class ReminderList : AppCompatActivity(), ReminderListAdapter.OnItemClickListener {
    private val list = ArrayList<ReminderListItem>()

    override fun onItemClicked(item:ReminderListItem, position: Int) {
        //TODO: intent and remove toast
        Toast.makeText(this, item.reminderName, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_list)

        val buttonInsert = findViewById<Button>(R.id.button_insert);
        //val buttonRemove = findViewById<Button>(R.id.button_remove);

        generateDummyList(10)
        buildRecyclerView()

        buttonInsert.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                list.add(0, ReminderListItem("INSERTED ITEM", "100", "steps", "SA", "SO", "12:00", "18:00"))
                recycler_view.adapter?.notifyItemInserted(0)
            }
        })

//        buttonRemove.setOnClickListener(object : View.OnClickListener{
//            override fun onClick(v: View?) {
//                list.removeAt(0)
//                recycler_view.adapter?.notifyItemRemoved(0)
//            }
//        })
    }

    private fun generateDummyList(size: Int){
        for (i in 0 until size) {
            val item = ReminderListItem("Dummy Name", "30", "min", "MO", "-  "+"FR", "10:00", "-  "+"17:00")
            list.add(item)
        }
    }

    private fun buildRecyclerView(){
        recycler_view.adapter = ReminderListAdapter(list, this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
    }
}
