package com.tandiera.project.elearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.tiagohm.markdownview.MarkdownView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.tandiera.project.elearning.databinding.LayoutMarkdownBinding
import com.tandiera.project.elearning.databinding.LayoutYoutubeBinding
import com.tandiera.project.elearning.model.PartsPage

class PartsPageAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_YOUTUBE = 1
        private const val TYPE_MARKDOWN = 2

        private const val YOUTUBE = "youtube"
    }

    var partsPage = mutableListOf<PartsPage>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class YouTubeViewHolder(youtubeBinding: LayoutYoutubeBinding) : RecyclerView.ViewHolder(youtubeBinding.root) {

    }

    class MarkdownViewHolder(markdownBinding: LayoutMarkdownBinding) : RecyclerView.ViewHolder(markdownBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_YOUTUBE -> {
                val youtubeBinding = LayoutYoutubeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                YouTubeViewHolder(youtubeBinding)
            } else -> {
                val markdownBinding = LayoutMarkdownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MarkdownViewHolder(markdownBinding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(partsPage[position].type) {
            YOUTUBE -> TYPE_YOUTUBE
            else -> TYPE_MARKDOWN
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}