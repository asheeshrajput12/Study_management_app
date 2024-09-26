package com.personalproject.studymanagement.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.personalproject.studymanagement.R
import com.personalproject.studymanagement.common.CommonFunctions
import com.personalproject.studymanagement.databinding.ActivityAddProjectBinding

class ActivityAddProject : AppCompatActivity() {
    private lateinit var activityAddProjectBinding: ActivityAddProjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityAddProjectBinding = ActivityAddProjectBinding.inflate(layoutInflater)
        setContentView(activityAddProjectBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
    }
}