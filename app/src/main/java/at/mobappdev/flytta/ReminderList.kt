package at.mobappdev.flytta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_reminder_list.*

class ReminderList : AppCompatActivity(), Adapter.OnItemClickListener {
    private val list = ArrayList<Item>()

    override fun onItemClicked(item:Item, position: Int) {
        //TODO: intent and remove toast
        Toast.makeText(this, item.text1, Toast.LENGTH_SHORT).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_list)

        val buttonInsert = findViewById<Button>(R.id.button_insert);
        val buttonRemove = findViewById<Button>(R.id.button_remove);

        generateDummyList(5)
        buildRecyclerView()

        buttonInsert.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                list.add(0, Item("new item at position "+0, "this is the second line"))
                recycler_view.adapter?.notifyItemInserted(0)
            }
        })

        buttonRemove.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                list.removeAt(0)
                recycler_view.adapter?.notifyItemRemoved(0)
            }
        })
    }

    private fun generateDummyList(size: Int){
        for (i in 0 until size) {
            val item = Item("Item $i", "Line 2")
            list.add(item)
        }
    }

    private fun buildRecyclerView(){
        recycler_view.adapter = Adapter(list, this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
    }
}
