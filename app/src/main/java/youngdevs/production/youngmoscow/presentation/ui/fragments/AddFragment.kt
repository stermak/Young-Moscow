package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.common.BaseFragment
import youngdevs.production.youngmoscow.data.entities.User
import youngdevs.production.youngmoscow.databinding.FragmentAddBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.UserViewModel


@AndroidEntryPoint
class AddFragment : BaseFragment<FragmentAddBinding>(FragmentAddBinding::inflate) {

    private val userViewModel: UserViewModel by viewModels()

    override fun listeners() {

        binding.btnAdd.setOnClickListener {
            insertDataToDatabase()
        }
    }

    private fun insertDataToDatabase() {

        val name = binding.name.text.toString()
        val email = binding.email.text.toString()
        val id = binding.id.text

        if(inputChecks()){
            Toast.makeText(context, "fill all fields!", Toast.LENGTH_SHORT).show()
        }else{
            //create user obj
            val user = User(0, name, email, Integer.parseInt(id.toString()))
            //add data to database
            userViewModel.addUser(user)
            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
            findNavController().navigate(AddFragmentDirections.actionAddFragmentToUsersFragment())
        }
    }

    private fun inputChecks(): Boolean = with(binding) {
        return@with binding.name.text.toString().isEmpty() ||
                binding.email.text.toString().isEmpty() ||
                binding.id.text.toString().isEmpty()
    }

    override fun init() {
    }

    override fun observers() {
    }

}