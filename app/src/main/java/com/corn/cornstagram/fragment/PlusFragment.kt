package com.corn.cornstagram.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.corn.cornstagram.R
import com.corn.cornstagram.adapter.ContentDTO
import com.corn.cornstagram.databinding.FragmentPlusBinding
import com.corn.cornstagram.databinding.ItemDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PlusFragment : Fragment() {
    private var _binding: FragmentPlusBinding? = null
    private val binding get() = _binding!!
    private var firestore: FirebaseFirestore? = null
    private var uid: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        _binding = FragmentPlusBinding.inflate(inflater, container, false)
        binding.frgDetailViewRecyclerview.adapter = DetailRecyclerViewAdapter()
        binding.frgDetailViewRecyclerview.layoutManager

        return binding.root
    }

    inner class DetailRecyclerViewAdapter: RecyclerView.Adapter<DetailRecyclerViewAdapter.DetailViewHolder>() {
        var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("images")?.orderBy("timestamp")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    contentDTOs.clear()
                    contentUidList.clear()

                    if (querySnapshot == null) return@addSnapshotListener

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(ContentDTO::class.java)
                        contentDTOs.add(item!!)
                        contentUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
            val binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DetailViewHolder(binding)
        }
        inner class DetailViewHolder(val binding: ItemDetailBinding) :RecyclerView.ViewHolder(binding.root)

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
            val contentDTO = contentDTOs[position]

            holder.binding.apply {

                detailViewitemProfileTxt.text = contentDTO.userId

                Glide.with(holder.itemView.context).load(contentDTO.imageUrl)
                    .into(detailViewitemProfileContent)

                detailViewitemExplainTxt.text = contentDTO.explain

                detailViewitemFavoriteTxt.text = "Likes ${contentDTO.favoriteCount}"

                Glide.with(holder.itemView.context).load(contentDTO.imageUrl)
                    .into(detailViewitemProfileImg)

                detailViewitemFavoriteImg.setOnClickListener {
                    favoriteAlarm(position)
                }

                if (contentDTOs!![position].favorites.containsKey(uid)) {
                    detailViewitemFavoriteImg.setImageResource(R.drawable.megalizamon)
                } else {
                    detailViewitemFavoriteImg.setImageResource(R.drawable.impmon)
                }
            }
        }
        private fun favoriteAlarm(position: Int) {
            val tsDoc = firestore?.collection("images")?.document(contentUidList[position])
            firestore?.runTransaction { transaction ->
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                val contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

                if (contentDTO!!.favorites.containsKey(uid)) {
                    contentDTO.favoriteCount = contentDTO.favoriteCount - 1
                } else {
                    contentDTO.favoriteCount = contentDTO.favoriteCount + 1
                }
                transaction.set(tsDoc, contentDTO)
            }
        }
    }
}