import androidx.viewbinding.ViewBinding

interface BaseAdapterCallback<T> {
    fun onItemClick(model: T, view: ViewBinding)
}