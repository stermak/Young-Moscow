package youngdevs.production.youngmoscow.application.ui.update

import android.app.AlertDialog
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.data.entities.User
import youngdevs.production.youngmoscow.application.ui.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.application.common.BaseFragment
import youngdevs.production.youngmoscow.databinding.FragmentUpdateBinding


@AndroidEntryPoint
class UpdateFragment : BaseFragment<FragmentUpdateBinding>(FragmentUpdateBinding::inflate) {

    private val args by navArgs<UpdateFragmentArgs>()
    private val userViewModel : UserViewModel by viewModels()

    override fun listeners() {
    }

    override fun init() {
        binding.apply {
            name.setText(args.currentUser.name)
            email.setText(args.currentUser.email)
            id.setText(args.currentUser.id.toString())
        }
        binding.btnUpdate.setOnClickListener {
            updateUser()
        }
    }

    private fun updateUser(){
        val firstName = binding.name.text.toString()
        val lastName = binding.email.text.toString()
        val age = binding.id.text.toString().toInt()

        if (inputChecks()){
            Toast.makeText(context, "field all fields!", Toast.LENGTH_SHORT).show()
        }else{
            val user = User(args.currentUser.id, firstName, lastName, age)
            userViewModel.updateUser(user)
            findNavController().navigate(UpdateFragmentDirections.actionUpdateFragmentToUsersFragment())
            Toast.makeText(context, "User updated successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputChecks(): Boolean = with(binding) {
        return@with binding.name.text.toString().isEmpty() ||
                binding.email.text.toString().isEmpty() ||
                binding.id.text.toString().isEmpty()
    }

    override fun observers() {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete)
            deleteUser()
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes"){ _,_ ->
            userViewModel.deleteUser(user = args.currentUser)
            Toast.makeText(context, "${args.currentUser.name} successfully deleted!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(UpdateFragmentDirections.actionUpdateFragmentToUsersFragment())
        }
        builder.setNegativeButton("No"){ _,_ -> }
        builder.setTitle("Delete ${args.currentUser.name}?")
        builder.setMessage("Are you sure to delete ${args.currentUser.name}?")
        builder.create()
            .show()
    }
}