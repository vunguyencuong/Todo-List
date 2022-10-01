package com.example.todoapp.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentRecycleBinBinding
import com.example.todoapp.viewmodel.TaskViewModel
import com.google.android.material.snackbar.Snackbar


class RecycleBinFragment : Fragment() {

    private lateinit var binding: FragmentRecycleBinBinding
    private lateinit var adapter: TaskAdapter
    //private lateinit var viewModel : TaskViewModel
    private val viewModel: TaskViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //setUpViewModel()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setUpAdapter()
        observing()
        setOnSwipeItem()
        Log.i("BugBin", "onViewCreated: Bin")
        viewModel.updateRecycleBinList()

    }

//    private fun setUpViewModel() {
//        viewModel =
//            ViewModelProvider(
//                requireActivity(),
//                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
//            )[TaskViewModel::class.java]
//    }

    private fun setUpAdapter() {
        val taskOnClickListener = TaskClickListener { }

        adapter = TaskAdapter(taskOnClickListener,0) {
            Log.i("CheckBox", "setUpAdapter: update ")
            it.isArchived = 0
            viewModel.unArchivedTask(it)
        }
        viewModel.updateRecycleBinList()
        binding.recyclerView.adapter = adapter
    }

    private fun observing() {
        viewModel.taskLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
    }

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRecycleBinBinding.inflate(inflater,container,false)
        return binding.root

    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_menu_bin,menu)
        val searchItem = menu.findItem(R.id.action_search_bin)
        val searchView = searchItem.actionView as SearchView
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
            R.id.action_sort_bin -> viewModel.sortTask()

            R.id.action_delete_all_bin-> deleteAllItem()

        }
        return super.onOptionsItemSelected(item)
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

