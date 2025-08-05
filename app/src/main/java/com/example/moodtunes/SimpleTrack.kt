package com.example.moodtunes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtunes.MoodActivity.LastfmTrack

data class SimpleTrack(val name: String, val artist: String)

class TrackAdapter(
    private var items: List<LastfmTrack>
) : RecyclerView.Adapter<TrackAdapter.TrackVH>() {

    inner class TrackVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle  = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvArtist = itemView.findViewById<TextView>(R.id.tvArtist)

        fun bind(track: LastfmTrack) {
            tvTitle.text  = track.name
            tvArtist.text = track.artist.name
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_track, parent, false)
        return TrackVH(view)
    }

    override fun onBindViewHolder(holder: TrackVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateTracks(newList: List<LastfmTrack>) {
        items = newList
        notifyDataSetChanged()
    }
}