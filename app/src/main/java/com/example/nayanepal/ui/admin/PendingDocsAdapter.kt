package com.example.nayanepal.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nayanepal.R
import com.example.nayanepal.data.model.VerificationDocument
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PendingDocsAdapter(private val onItemClick: (VerificationDocument) -> Unit) :
    ListAdapter<VerificationDocument, PendingDocsAdapter.DocViewHolder>(DocDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pending_doc, parent, false)
        return DocViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: DocViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DocViewHolder(itemView: View, private val onItemClick: (VerificationDocument) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        
        private val ivThumbnail: ImageView = itemView.findViewById(R.id.ivDocThumbnail)
        private val tvType: TextView = itemView.findViewById(R.id.tvDocType)
        private val tvUserId: TextView = itemView.findViewById(R.id.tvUserId)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTimestamp)

        fun bind(doc: VerificationDocument) {
            tvType.text = doc.documentType.replaceFirstChar { it.uppercase() }
            tvUserId.text = "User ID: ${doc.userId}"
            
            val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
            tvTime.text = sdf.format(Date(doc.createdAt))
            // In a real app, you'd use a full storage URL. 
            // For now, using the storage path as a reference.
            // Note: Firebase Storage URIs need proper handling for Coil.
            // This is a placeholder.
            ivThumbnail.load(R.drawable.ic_launcher_foreground) 
            itemView.setOnClickListener { onItemClick(doc) }
        }
    }

    class DocDiffCallback : DiffUtil.ItemCallback<VerificationDocument>() {
        override fun areItemsTheSame(oldItem: VerificationDocument, newItem: VerificationDocument): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VerificationDocument, newItem: VerificationDocument): Boolean {
            return oldItem == newItem
        }
    }
}
