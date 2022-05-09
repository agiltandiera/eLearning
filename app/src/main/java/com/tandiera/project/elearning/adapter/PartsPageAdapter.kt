package com.tandiera.project.elearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.tiagohm.markdownview.MarkdownView
import br.tiagohm.markdownview.css.styles.Github
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
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

    class YouTubeViewHolder(private val youtubeBinding: LayoutYoutubeBinding) : RecyclerView.ViewHolder(youtubeBinding.root) {
        fun bindItem(partPage: PartsPage) {
            youtubeBinding.ytContent.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.cueVideo(partPage.content.toString(), 0F)
                }
            })
        }

    }

    class MarkdownViewHolder(private val markdownBinding: LayoutMarkdownBinding) : RecyclerView.ViewHolder(markdownBinding.root) {
        fun bindItem(partPage: PartsPage) {
            val css = Github()
            css.addRule("body", "color: black",
                "font-family: sans-serif",
                "padding: 0px",
                "background-color: #fafafa")
            markdownBinding.mdContent.addStyleSheet(css)
            markdownBinding.mdContent.loadMarkdown(partPage.content)
        }

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
        val partPage = partsPage[position]
        when(getItemViewType(position)) {
            TYPE_YOUTUBE -> (holder as YouTubeViewHolder).bindItem(partPage)
            TYPE_MARKDOWN -> (holder as MarkdownViewHolder).bindItem(partPage)
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}