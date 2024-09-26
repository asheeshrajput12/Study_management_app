package com.personalproject.studymanagement.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.personalproject.studymanagement.R
import com.personalproject.studymanagement.databinding.DialogSearchableListBinding

class CustomSearchableListDialog : DialogFragment() {
    interface OnItemSelectedListener {
        fun onItemSelected(item: String)
    }
    private var listener: OnItemSelectedListener? = null
    private lateinit var items: List<String>
    private lateinit var adapter: ArrayAdapter<String>
    private var _binding: DialogSearchableListBinding? = null
    private val binding get() = _binding!!


    private var labelBackgroundColor: Int? = null
    private var titleTextColor: Int? = null
    private var titleText: String? = null

    companion object {
        fun newInstance(items: List<String>): CustomSearchableListDialog {
            val dialog = CustomSearchableListDialog()
            dialog.items = items
            return dialog
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val params: WindowManager.LayoutParams = window.attributes

            // Get screen width
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels

            // Convert 10 dp to pixels
            val marginInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, displayMetrics).toInt()

            // Calculate new width
            val newWidth = screenWidth - (2 * marginInPx)

            // Set the new width
            params.width = newWidth

            window.attributes = params
            window.setLayout(params.width, ViewGroup.LayoutParams.MATCH_PARENT)
            window.setBackgroundDrawableResource(R.drawable.transparent_background)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSearchableListBinding.inflate(inflater, container, false)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        binding.listView.adapter = adapter
        // Set divider
        binding.listView.divider = ColorDrawable(Color.GRAY); // Set color or drawable
        binding.listView.dividerHeight = 1; // Height of the divider

        binding.searchEditText.addTextChangedListener {
            adapter.filter.filter(it.toString())
        }
        binding.ivCancelDialog.setOnClickListener{
            dismiss()
        }

        binding.listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            listener?.onItemSelected(adapter.getItem(position) ?: "")
            dismiss()
        }

        dialog?.setTitle("Select Item")
        dialog?.window?.setBackgroundDrawableResource(android.R.color.white)
        applyConfigurations()
        return binding.root
    }
    private fun applyConfigurations() {
        labelBackgroundColor?.let {
            binding.llLabel.setBackgroundColor(it)
        }
        titleTextColor?.let {
            binding.tvTitle.setTextColor(it)
            ImageViewCompat.setImageTintList(binding.ivCancelDialog, android.content.res.ColorStateList.valueOf(it))
        }
        titleText?.let {
            binding.tvTitle.text = it
        }
    }
    fun setLabelBackgroundColor(color: Int) {
        labelBackgroundColor = color
    }

    fun setTitleTextColor(color: Int) {
        titleTextColor = color
    }

    fun setTitleText(text: String) {
        titleText = text
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnItemSelectedListener
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}