package youngdevs.production.youngmoscow.application.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.entities.User
import youngdevs.production.youngmoscow.domain.usecases.AddUserUseCase
import youngdevs.production.youngmoscow.domain.usecases.DeleteUserUseCase
import youngdevs.production.youngmoscow.domain.usecases.GetUsersUseCase
import youngdevs.production.youngmoscow.domain.usecases.UpdateUserUseCase
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor
    (private val addUserUseCase: AddUserUseCase,
     private val deleteUserUseCase: DeleteUserUseCase,
     private val updateUserUseCase: UpdateUserUseCase,
     private val getUsersUseCase: GetUsersUseCase,
) : ViewModel(){

    val readAllData: Flow<List<User>> = getUsersUseCase.readAllData

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            addUserUseCase.addUser(user)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            updateUserUseCase.updateUser(user)
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch(Dispatchers.IO){
            deleteUserUseCase.deleteUser(user)
        }
    }

    fun deleteAllUser(){
        viewModelScope.launch(Dispatchers.IO){
            deleteUserUseCase.deleteAllUsers()
        }
    }
}