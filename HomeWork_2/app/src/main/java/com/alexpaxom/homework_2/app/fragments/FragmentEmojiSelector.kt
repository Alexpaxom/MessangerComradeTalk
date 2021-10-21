package com.alexpaxom.homework_2.app.fragments

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.alexpaxom.homework_2.app.adapters.BaseAdapterCallback
import com.alexpaxom.homework_2.app.adapters.EmojiSelectorAdapter
import com.alexpaxom.homework_2.databinding.EmojiSelectorBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentEmojiSelector : BottomSheetDialogFragment() {

    private var _binding: EmojiSelectorBottomDialogBinding? = null
    private val binding get() = _binding!!

    private val emojiSelectorAdapter = EmojiSelectorAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View {
        _binding = EmojiSelectorBottomDialogBinding.inflate(inflater, container, false)

        emojiSelectorAdapter.updateItems(listOf(
            "\uD83D\uDE00",
            "\uD83D\uDE05",
            "\uD83E\uDD23",
            "\uD83D\uDE07",
            "\uD83D\uDE1B",
            "\uD83E\uDD2A",
            "\uD83D\uDE11",
        ))

        emojiSelectorAdapter.attachCallback(object :BaseAdapterCallback<String> {
            override fun onItemClick(model: String, view: ViewBinding) {
                returnResult(model)
            }

            override fun onLongClick(model: String, view: ViewBinding): Boolean {
                return true
            }

        })

        binding.selectorEmojiList.adapter = emojiSelectorAdapter

        binding.selectorEmojiList.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.selectorEmojiList.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val cellWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    SIZE_OF_EMOJI_VIEW_IN_DP,
                    Resources.getSystem().displayMetrics).toInt()


                binding.selectorEmojiList.layoutManager = GridLayoutManager(
                    context,
                    binding.selectorEmojiList.measuredWidth/cellWidth
                )
            }

        })

        return binding.root
    }




    private fun returnResult(emojiUnicode: String) {
        val resultId = arguments?.getInt(RESULT_ID)
        requireNotNull(resultId)

        val result = Bundle()
        result.putInt(RESULT_ID, resultId)
        result.putString(EMOJI_UNICODE, emojiUnicode)
        parentFragmentManager.setFragmentResult(EMOJI_SELECT_RESULT_DIALOG_ID, result)

        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun newInstance(resultId: Int) : FragmentEmojiSelector {
            val params = Bundle()
            params.putInt(RESULT_ID, resultId)

            val fragmentEmojiSelector = FragmentEmojiSelector()
            fragmentEmojiSelector.arguments = params

            return fragmentEmojiSelector
        }

        const val EMOJI_SELECT_RESULT_DIALOG_ID = "com.alexpaxom.EMOJI_SELECT_RESULT_DIALOG_ID"
        const val RESULT_ID = "com.alexpaxom.RESULT_ID"
        const val EMOJI_UNICODE = "com.alexpaxom.EMOJI_UNICODE"
        const val SIZE_OF_EMOJI_VIEW_IN_DP = 50f
    }

}