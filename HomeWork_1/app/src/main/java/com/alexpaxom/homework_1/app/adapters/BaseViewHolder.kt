import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T>(var binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(model: T)
}