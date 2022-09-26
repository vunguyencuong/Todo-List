package com.example.todoapp.ui

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar


class TaskFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var binding: FragmentTaskBinding
    private lateinit var adapter: TaskAdapter
    private var isHidden = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViewModel()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setUpAdapter()

        observing()

        setOnClickListeners()

        setOnSwipeItem()


    }


    private fun TaskFragment.setOnSwipeItem() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
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
                    setAction("Undo") {
                        viewModel.addTask(taskItem)
                    }
                    show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)
        setHasOptionsMenu(true)
    }

    private fun setOnClickListeners() {
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_addFragment)
        }
    }

    private fun observing() {
        viewModel.taskLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
    }

    private fun setUpAdapter() {
        val taskOnClickListener = TaskClickListener { taskItem ->
            findNavController().navigate(
                TaskFragmentDirections.actionTaskFragmentToUpdateFragment(taskItem)
            )
        }

        adapter = TaskAdapter(taskOnClickListener) {
            viewModel.updateTask(it)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )[TaskViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(inflater)
        return binding.root
    }



    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_menu,menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        menu.findItem(R.id.action_hide_completed_tasks).isChecked =  (viewModel.getHidden() == 1)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && !newText.isEmpty()) {
                    viewModel.searchTitle(newText)
                } else viewModel.updateLiveData()
                return true
            }
        })
    }



    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_sort -> viewModel.sortTask()

            R.id.action_delete_all-> deleteAllItem()
            R.id.action_hide_completed_tasks ->hideCompletedTask(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hideCompletedTask(menuItem : MenuItem) {
        if(isHidden == 0) {
            isHidden = 1
            menuItem.isChecked = true

        }else {
            isHidden = 0
            menuItem.isChecked = false
        }
        viewModel.updateHidden(isHidden)
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