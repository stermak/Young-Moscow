package youngdevs.production.youngmoscow.application.ui.users

import android.app.AlertDialog
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.application.ui.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.application.adapter.UserAdapter
import youngdevs.production.youngmoscow.application.common.BaseFragment
import youngdevs.production.youngmoscow.databinding.FragmentUsersBinding


@AndroidEntryPoint
class UsersFragment : BaseFragment<FragmentUsersBinding>(FragmentUsersBinding::inflate) {

    private lateinit var userAdapter: UserAdapter
    private val userViewModel: UserViewModel by viewModels()

    override fun listeners() {

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(UsersFragmentDirections.actionUsersFragmentToAddFragment())
        }
    }

    override fun init() {

        initRecycler()

    }
    private fun initRecycler(){

        userAdapter = UserAdapter()

        binding.rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun observers() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                userViewModel.readAllData.collect{
                    userAdapter.submitList(it)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete)
            deleteAllUsers()
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUsers() {
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes"){ _,_ ->
            userViewModel.deleteAllUser()
            Toast.makeText(context, "Users successfully deleted!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.setTitle("Delete all users")
        builder.setMessage("Are you sure to delete all users?")
        builder.create()
            .show()
    }
}