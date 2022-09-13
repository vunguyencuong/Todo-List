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
import com.example.todoapp.databinding.FragmentUpdateBinding
import com.example.todoapp.viewmodel.TaskViewModel

class UpdateFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentUpdateBinding.inflate(inflater)

        val args = UpdateFragmentArgs.fromBundle(requireArguments())

        binding.apply {
            edtUpdateTask.setText(args.taskItem.title)
            spinnerUpdatePriorities.setSelection(args.taskItem.priority)

            btnUpdate.setOnClickListener {
                if(TextUtils.isEmpty(edtUpdateTask.text)){
                    Toast.makeText(requireContext(),"It's empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val task_str = edtUpdateTask.text
                val priority = spinnerUpdatePriorities.selectedItemPosition

                val taskItem = TaskItem(
                    args.taskItem.tId,
                    task_str.toString(),
                    priority,
                    args.taskItem.timestamp
                )

                viewModel.updateTask(taskItem)
                Toast.makeText(requireContext(),"Successfully Updated", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateFragment_to_taskFragment)
            }
        }

        return binding.root
    }

}