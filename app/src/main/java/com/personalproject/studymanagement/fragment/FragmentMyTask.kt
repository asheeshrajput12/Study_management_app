package com.personalproject.studymanagement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.personalproject.studymanagement.adapter.AdapterDateListMyTask
import com.personalproject.studymanagement.common.MyViewModel
import com.personalproject.studymanagement.databinding.LayoutMyTaskFragmentBinding
import kotlin.jvm.java

class FragmentMyTask : Fragment() {

    private lateinit var binding: LayoutMyTaskFragmentBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var taskAdapter: AdapterDateListMyTask

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
                LayoutMyTaskFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        taskAdapter = AdapterDateListMyTask(emptyList()) // Initialize with empty list
        binding.rvDateList.adapter = taskAdapter
        binding.rvDateList.layoutManager=LinearLayoutManager(context)

    }
}