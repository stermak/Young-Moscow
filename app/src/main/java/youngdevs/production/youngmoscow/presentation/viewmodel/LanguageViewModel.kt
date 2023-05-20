package youngdevs.production.youngmoscow.presentation.viewmodel

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor() : ViewModel() {

    companion object {
        fun changeLanguage(context: Context, languageCode: String) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val config = Configuration()
            config.setLocale(locale)

            context.resources.updateConfiguration(config, context.resources.displayMetrics)

            val editor = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE).edit()
            editor.putString("SelectedLanguage", languageCode)
            editor.apply()
        }
    }
}
