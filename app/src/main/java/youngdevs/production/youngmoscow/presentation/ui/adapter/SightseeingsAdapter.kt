package youngdevs.production.youngmoscow.presentation.ui.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.entities.Sightseeing
import youngdevs.production.youngmoscow.data.utilities.RetrofitClient
import youngdevs.production.youngmoscow.databinding.ItemSightseeingBinding

class SightseeingsAdapter(
    private val scope: LifecycleCoroutineScope
) : ListAdapter<Sightseeing, SightseeingsAdapter.SightseeingViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SightseeingViewHolder {
        val binding = ItemSightseeingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SightseeingViewHolder(binding, scope)
    }

    override fun onBindViewHolder(holder: SightseeingViewHolder, position: Int) {
        val sightseeing = getItem(position)
        holder.bind(sightseeing)
    }

    class SightseeingViewHolder(
        private val binding: ItemSightseeingBinding,
        private val scope: LifecycleCoroutineScope
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sightseeing: Sightseeing) {
            binding.name.text = sightseeing.name
            binding.description.text = sightseeing.description
            binding.address.text = sightseeing.address
            loadImage(sightseeing.image)
        }

        private fun loadImage(imageName: String) {
            val imagesService = RetrofitClient.imagesService
            scope.launch {
                try {
                    val response = imagesService.getImage(imageName.trim()) // Убедитесь, что нет пробелов перед именем файла
                    if (response.isSuccessful) {
                        val inputStream = response.body()?.byteStream()
                        inputStream?.let {
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            binding.image.setImageBitmap(bitmap)
                        }
                    } else {
                        Log.e("SightseeingsAdapter", "Failed to load image: $imageName")
                    }
                } catch (e: Exception) {
                    Log.e("SightseeingsAdapter", "Error loading image: $imageName", e)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Sightseeing>() {
        override fun areItemsTheSame(oldItem: Sightseeing, newItem: Sightseeing): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sightseeing, newItem: Sightseeing): Boolean {
            return oldItem == newItem
        }
    }
}

