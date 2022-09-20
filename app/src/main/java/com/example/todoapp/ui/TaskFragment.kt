package com.example.todoapp.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.TaskItem
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar


class TaskFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTaskBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        adapter = TaskAdapter(TaskClickListener { taskItem ->
            findNavController().navigate(TaskFragmentDirections.actionTaskFragmentToUpdateFragment(taskItem))
        }) {
            viewModel.updateTask(it)

        }

        viewModel.getAllTasks.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }


        binding.apply {

            binding.recyclerView.adapter = adapter

            btnAdd.setOnClickListener{
                findNavController().navigate(R.id.action_taskFragment_to_addFragment)
            }
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskItem = adapter.currentList[position]
                viewModel.deleteTask(taskItem)

                Snackbar.make(binding.root, "Deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.addTask(taskItem)
                    }
                    show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)
        setHasOptionsMenu(true)

        return binding.root
    }



    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_menu,menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    runQuery(newText)
                }
                return true
            }

        })
    }

    fun runQuery(query: String){
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_sort -> viewModel.getAllPriorityTasks.observe(viewLifecycleOwner) { tasks ->
                adapter.submitList(tasks)
            }
            R.id.action_delete_all-> deleteAllItem()
            R.id.action_hide_completed_tasks ->hideCompletedTask()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hideCompletedTask() {
        viewModel.readNotDoneData().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }
    private  fun deleteAllItem(){
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes"){
                dialog, _-> viewModel.deleteAll()
                dialog.dismiss()
            }.setNegativeButton("No"){
                dialog,_-> dialog.dismiss()
            }.create().show()
    }


}