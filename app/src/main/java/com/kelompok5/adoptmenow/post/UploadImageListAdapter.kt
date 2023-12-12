package com.kelompok5.adoptmenow.post

import android.content.ContentResolver
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kelompok5.adoptmenow.R
import com.kelompok5.adoptmenow.bindImage
import com.kelompok5.adoptmenow.databinding.ListItemUploadPostImageBinding

private const val IMAGE_COUNT = 6

class UploadImageListAdapter(
    private val resources: Resources,
    private val contentResolver: ContentResolver,
    private val changeImage: (Int) -> Unit,
    oldImage: List<String> = listOf()
): ListAdapter<Uri?, UploadImageListAdapter.ImageViewHolder>(DiffCallback) {

    private val items = MutableList<Uri?>(IMAGE_COUNT) { null }

    init {
        var i = 0
        for(item in oldImage) {
            items[i++] = Uri.Builder()
                .scheme("gs")
                .path(item)
                .build()
        }
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ListItemUploadPostImageBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageView = holder.binding.image
        val item = items[position]
        if(item != null) {
            if(item.scheme == "gs") {
                bindImage(imageView, item.path)
            } else {
                val img = contentResolver.openInputStream(item)
                imageView.setImageDrawable(BitmapDrawable(resources, img))
            }
        } else {
            imageView.setImageResource(R.drawable.ic_plus)
        }
        imageView.setOnClickListener { changeImage(position) }
    }

    fun updateImage(position: Int, uri: Uri?) {
        items[position] = uri
        notifyItemChanged(position)
    }

    class ImageViewHolder(
        val binding: ListItemUploadPostImageBinding) : RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<Uri?>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }
    }

}