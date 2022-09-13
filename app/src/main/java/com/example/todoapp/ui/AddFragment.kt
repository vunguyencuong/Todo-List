package com.example.todoapp.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.database.TaskItem
import com.example.todoapp.databinding.FragmentAddBinding
import com.example.todoapp.viewmodel.TaskViewModel
import android.widget.ArrayAdapter as ArrayAdapter

class AddFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAddBinding.inflate(inflater)

        val myAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.priorities)
            
        )

        binding.apply {
            spinnerPriorities.adapter = myAdapter
            btnSave.setOnClickListener {
                if(TextUtils.isEmpty((edtTask.text))){
                    Toast.makeText(requireContext(), "It's empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val title_str = edtTask.text.toString()
                val priority = spinnerPriorities.selectedItemPosition

                val taskItem = TaskItem(
                    0,
                    title_str,
                    priority,
                    System.currentTimeMillis()
                )

                viewModel.addTask(taskItem)
                Toast.makeText(requireContext(),"Successfully added!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_taskFragment)
            }
        }

        return binding.root
    }



}