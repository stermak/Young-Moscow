package youngdevs.production.youthmoscow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import youngdevs.production.youthmoscow.R

class FavouritesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Buttons
        val BtnHome = view.findViewById<ImageButton>(R.id.BtnHome)
        val BtnMaps = view.findViewById<ImageButton>(R.id.BtnMaps)
        val BtnFavourites = view.findViewById<ImageButton>(R.id.BtnFavourites)
        val BtnProfile = view.findViewById<ImageButton>(R.id.BtnProfile)
        //Buttons Motions
        BtnHome.setOnClickListener { viewCreate: View? ->
            val bundleHome = Bundle()
            Navigation.findNavController(view).navigate(R.id.action_favouritesFragment_to_mainFragment, bundleHome)
        }
        BtnMaps.setOnClickListener { viewCreate: View? ->
            val bundleHome = Bundle()
            Navigation.findNavController(view).navigate(R.id.action_favouritesFragment_to_mapsFragment, bundleHome)
        }
        BtnFavourites.setOnClickListener { viewCreate: View? ->
            val bundleHome = Bundle()
            Navigation.findNavController(view).navigate(R.id.action_favouritesFragment_self, bundleHome)
        }
        BtnProfile.setOnClickListener { viewCreate: View? ->
            val bundleHome = Bundle()
            Navigation.findNavController(view).navigate(R.id.action_favouritesFragment_to_profileFragment, bundleHome)
        }
    }
}