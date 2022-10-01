package com.example.todoapp.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
   // private var isHidden = 0



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViewModel()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setUpAdapter()

        observing()

        setOnClickListeners()

        Log.i("BugBin", "onViewCreated: Tasks")
        setOnSwipeItem()

        viewModel.updateList()

    }

//    override fun onResume() {
//        viewModel.updateList()
//    }

    private fun setOnSwipeItem() {
        Log.i("Update Archived", "setOnSwipeItem: ")
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
                if(taskItem.isDone == 0){
                    Log.i("Update Archived", "onSwiped: deleted")
                    viewModel.deleteTask(taskItem)

                    Snackbar.make(binding.root,"${taskItem.title} has just been deleted",Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.addTask(taskItem)
                        }
                        show()
                    }
                }
                else{
                    Log.i("Update Archived", "onSwiped: move")
                    viewModel.moveDoneTaskToRecycleBin(taskItem)

                    Toast.makeText(
                        context,
                        "You have already done this task!",
                        Toast.LENGTH_LONG
                    ).show()
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

        adapter = TaskAdapter(taskOnClickListener,1) {
            Log.i("CheckBox", "setUpAdapter: update ")
            viewModel.updateTask(it)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProvider(
                requireActivity(),
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
        val searchItem = menu.findItem(R.id.action_search_menu)
        val searchView = searchItem.actionView as SearchView
//        menu.findItem(R.id.action_hide_completed_tasks).isChecked =  (viewModel.getHidden() == 1)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()) {
                    viewModel.searchTitle(newText)
                } else viewModel.updateLiveData()
                return true
            }
        })
    }



    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_sort_menu -> viewModel.sortTask()

           // R.id.action_delete_all-> deleteAllItem()
           // R.id.action_hide_completed_tasks ->hideCompletedTask(item)
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun hideCompletedTask(menuItem : MenuItem) {
//        Log.d("Hide function", "hideCompletedTask: Update")
//        if(isHidden == 0) {
//            isHidden = 1
//            menuItem.isChecked = true
//
//        }else {
//            isHidden = 0
//            menuItem.isChecked = false
//        }
//        viewModel.updateHidden(isHidden)
//    }


//    private  fun deleteAllItem(){
//        AlertDialog.Builder(requireContext())
//            .setTitle("Delete All")
//            .setMessage("Are you sure?")
//            .setPositiveButton("Yes"){
//                dialog, _-> viewModel.deleteAll()
//                dialog.dismiss()
//            }.setNegativeButton("No"){
//                dialog,_-> dialog.dismiss()
//            }.create().show()
//    }

}