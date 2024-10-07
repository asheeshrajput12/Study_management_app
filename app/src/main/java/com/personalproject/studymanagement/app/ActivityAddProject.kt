package com.personalproject.studymanagement.app

import android.content.ContentValues
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.personalproject.studymanagement.R
import com.personalproject.studymanagement.common.CommonFunctions
import com.personalproject.studymanagement.databinding.ActivityAddProjectBinding
import java.util.Calendar

class ActivityAddProject : AppCompatActivity() {
    private lateinit var activityAddProjectBinding: ActivityAddProjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddProjectBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_project)

        CommonFunctions.addMandatoryAsterisk(activityAddProjectBinding.tvProjectNameLabel)
        CommonFunctions.addMandatoryAsterisk(activityAddProjectBinding.tvProjectDescriptionLabel)
        activityAddProjectBinding.btnSave.setOnClickListener {
            saveProject()
        }
    }
    private fun saveProject() {
        val projectName = activityAddProjectBinding.etProjectName.text.toString()
        val projectDescription = activityAddProjectBinding.etProjectDescription.text.toString()

        // Validate input fields
        if (projectName.isEmpty()) {
            Toast.makeText(this, "Please enter a project name", Toast.LENGTH_SHORT).show()
            return
        }

        if (projectDescription.isEmpty()) {
            Toast.makeText(this, "Please enter a project description", Toast.LENGTH_SHORT).show()
            return
        }

        val values = ContentValues().apply {
            put("txt_name", activityAddProjectBinding.etProjectName.text.toString())
            put("description", activityAddProjectBinding.etProjectDescription.text.toString())
            put("txt_created_date", Calendar.getInstance().time.toString())
            put("txt_status", "Active")
            put("txt_priority", "High")
            put("txt_type", "Development")
            put("txt_due_date", "2024-12-31")
        }
        CommonFunctions.insertData(this,"tbl_project",values)
    }
}