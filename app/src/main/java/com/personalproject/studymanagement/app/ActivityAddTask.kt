package com.personalproject.studymanagement.app

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.personalproject.studymanagement.R
import com.personalproject.studymanagement.databinding.LayoutActivityAddTaskBinding

class ActivityAddTask : AppCompatActivity() {
    private lateinit var binding: LayoutActivityAddTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=LayoutActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        binding.btnAddTask.setOnClickListener {
            addTaskItem()
        }
    }
    private fun addTaskItem() {
        // Inflate the custom task layout
        val taskView = layoutInflater.inflate(R.layout.item_add_task, binding.llAddTask, false)

        // Initially set the taskView off-screen (to the left)
        taskView.translationX = -700.090f

        // Animate the taskView from left to right
        taskView.animate()
            .translationX(0f)  // Move it to its normal position
            .setDuration(300)  // Animation duration
            .start()

        // Add the inflated taskView to llAddTask
        binding.llAddTask.addView(taskView)

        // Optionally, set any data or listeners on the new task item here
        taskView.findViewById<ImageView>(R.id.ivDeleteTask).setOnClickListener {
            // Perform left swipe animation and then remove the view
            taskView.animate()
                .translationX(-taskView.width.toFloat())  // Swipe it off to the left
                .setDuration(300)  // Animation duration
                .withEndAction {
                    // Remove the task item from the layout after the animation ends
                    binding.llAddTask.removeView(taskView)
                }
                .start()
        }

        // Optionally, you can focus on the current task item after adding it
        taskView.requestFocus()
    }

}