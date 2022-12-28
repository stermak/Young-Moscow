package youngdevs.production.youthmoscow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import youngdevs.production.youthmoscow.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Buttons
        val btnHome = view.findViewById<ImageButton>(R.id.btnHome)
        val btnMaps = view.findViewById<ImageButton>(R.id.btnMaps)
        val btnFavourites = view.findViewById<ImageButton>(R.id.btnFavourites)
        val btnProfile = view.findViewById<ImageButton>(R.id.btnProfile)
        //Buttons Motions
        btnHome.setOnClickListener { viewCreate: View? ->
            val bundleHome = Bundle()
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_mainFragment, bundleHome)
        }
        btnMaps.setOnClickListener { viewCreate: View? ->
            val bundleHome = Bundle()
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_mapsFragment, bundleHome)
        }
        btnFavourites.setOnClickListener { viewCreate: View? ->
            val bundleHome = Bundle()
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_favouritesFragment, bundleHome)
        }
        btnProfile.setOnClickListener { viewCreate: View? ->
            val bundleHome = Bundle()
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_self, bundleHome)
        }
    }
}