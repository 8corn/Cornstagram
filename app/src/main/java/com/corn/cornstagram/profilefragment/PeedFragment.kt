package com.corn.cornstagram.profilefragment

import ProfilesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.corn.cornstagram.R
import com.corn.cornstagram.adapter.ContentDTO
import com.corn.cornstagram.adapter.Profiles
import com.corn.cornstagram.databinding.FragmentPeedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PeedFragment : Fragment() {
    private var _binding: FragmentPeedBinding ?= null
    private val binding get() = _binding!!

    private var firestore: FirebaseFirestore? = null
    private var auth: FirebaseAuth? = null
    private var uid: String? = null
    private var currentUserUid: String? = null
    private var contentDTOs: ArrayList<ContentDTO> = arrayListOf()

    companion object {
        var PICK_PROFILE_FROM_ALBUM = 10
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPeedBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        currentUserUid = auth?.currentUser?.uid

        uid = arguments?.getString("destinationUid")

        binding.peedRvProfile.layoutManager = GridLayoutManager(context, 3)
        binding.peedRvProfile.adapter = UserFragmentRecyclerViewAdapter()

//        여기서부터!! 모르겠지만 일단 해!! 피드를 만든 것을 가져와서 올려야 해

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileList = arrayListOf(
            Profiles(R.drawable.gilmon, "gilmon", "Gilmon", 2022),
            Profiles(R.drawable.megalizamon, "megalizamon", "megalizamon", 2024),
            Profiles(R.drawable.impmon, "impmon", "impmon", 2023),
            Profiles(R.drawable.agumon, "agumon", "agumon", 2024),
        )
        binding.peedRvProfile.layoutManager = GridLayoutManager(context, 3)
        binding.peedRvProfile.adapter = ProfilesAdapter(profileList)
    }
    inner class UserFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        init {
            firestore?.collection("images")?.whereEqualTo("uid", uid)?.addSnapshotListener { querySnapshot, _ ->
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot.documents) {
                    contentDTOs.add(snapshot.toObject(ContentDTO::class.java)!!)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val width = resources.displayMetrics.widthPixels / 3
            val imageView = ImageView(parent.context)
            imageView.layoutParams = LinearLayoutCompat.LayoutParams(width, width)
            return CustomViewHolder(imageView)
        }

        inner class CustomViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val imageView = (holder as CustomViewHolder).imageView
            Glide.with(holder.itemView.context).load(contentDTOs[position].imageUrl).apply(RequestOptions().centerCrop()).into(imageView)
        }
    }

}