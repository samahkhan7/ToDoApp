package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    // val are values that won't be changed again, whereas var can be changed
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {

                // 1. remove the item from the list
                listOfTasks.removeAt(position)

                // 2. notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        // detect when the user clicks on the add button
        // findViewById<Button>(R.id.button).setOnClickListener {
            // code below will be executed when the user clicks on a button
            //Log.i("Samah", "User clicked on button")
    //    }

        // listOfTasks.add("Do laundry")
        // listOfTasks.add("Go for a walk")

        // instead of hardcoding the initial tasks, we'll use loadItems() to populate the list of items
        loadItems()

        // Lookup the recyclerview in layout
        val recylerView = findViewById<RecyclerView>(R.id.recylerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // attach the adapter to recyclerView to populate items
        recylerView.adapter = adapter
        // set layout manager to position the items
        recylerView.layoutManager = LinearLayoutManager(this)


        // set up the button and input field, so that the user can enter a task and add it to the list
        val inputTextField = findViewById<EditText>(R.id.addTask)


        // get a reference to the button
        // and then set an onClickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. Grab the text the user has inputted into the edit text field -> addTask
            val userInputtedTask = inputTextField.text.toString()

            // 2. add the string to the list of tasks -> listOfTasks
            listOfTasks.add(userInputtedTask)

            // notify the adapter that the data has been updated
            // size - 1 to add item at the end of the list
            adapter.notifyItemInserted(listOfTasks.size-1)

            // 3. clear out the field (reset it)
            inputTextField.setText("")

            saveItems()
        }
    }

    // save the data that the user has inputted
    // save data by writing and reading from a file

    // create a method to get the file we need
    fun getDataFile() : File {

        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // loads the item by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // saves items by writing them into our data file
    fun saveItems() {
        // try catch block to avoid crashing the app
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}