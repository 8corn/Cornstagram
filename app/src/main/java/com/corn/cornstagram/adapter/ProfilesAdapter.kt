import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.corn.cornstagram.adapter.Profiles
import com.corn.cornstagram.databinding.ListItemBinding

class CustomViewHolder(val binding: ListItemBinding):
        RecyclerView.ViewHolder(binding.root)

class ProfilesAdapter  (val profileList: ArrayList<Profiles>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CustomViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as CustomViewHolder).binding

        binding.itemPeed.setImageResource(profileList[position].image)
    }
}

