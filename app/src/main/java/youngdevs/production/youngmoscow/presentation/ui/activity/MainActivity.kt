package youngdevs.production.youngmoscow.presentation.ui.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.ActivityMainBinding
import youngdevs.production.youngmoscow.presentation.ui.fragments.MainFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // Объект, который связывает элементы интерфейса в макете с кодом в MainActivity
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инфлейт макета ActivityMainBinding и установка его в качестве контента активности
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка BottomNavigationView для навигации по приложению
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        // Настройка OnBackPressedCallback для обработки нажатия на кнопку "назад" на устройстве
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Если текущее назначение - MainFragment, то передается управление его методу handleOnBackPressed()
                // В противном случае - навигация вверх по стеку фрагментов
                if (navController.currentDestination?.id == R.id.mainFragment) {
                    val navHostFragment =
                        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as? NavHostFragment
                    val mainFragment =
                        navHostFragment?.childFragmentManager?.fragments?.firstOrNull { it is MainFragment } as? MainFragment
                    mainFragment?.handleOnBackPressed() ?: run { finish() }
                } else {
                    navController.navigateUp()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}