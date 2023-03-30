package youngdevs.production.youngmoscow.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class YoungMoscow : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("f73729a2-ece7-42f6-a70a-20e6e1033123")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}