package com.alexpaxom.homework_2.app.features.menubottom.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexpaxom.homework_2.app.features.menubottom.adapters.MenuAdapter
import com.alexpaxom.homework_2.databinding.FragmentBottomMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomMenuFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomMenuBinding.inflate(inflater, container, false)

        binding.bottomMenu.layoutManager = LinearLayoutManager(context)

        val popupMenu = PopupMenu(context, null)
        val menu = popupMenu.menu

        popupMenu.menuInflater.inflate(
            arguments?.getInt(ARGUMENT_MENU_ID) ?: error("Need menu id"),
            menu
        )

        binding.bottomMenu.adapter = MenuAdapter(
            menu = menu,
            onMenuItemClickListener = {
                returnResult(it.itemId)
            }
        )

        return binding.root
    }

    private fun returnResult(menuItemId: Int) {
        val result = Bundle().apply {
            putParcelable(
                RESULT_PARAM_CALLBACK_PARAMS,
                arguments?.getParcelable(RESULT_PARAM_CALLBACK_PARAMS)
            )
            putInt(RESULT_PARAM_MENU_ITEM_ID, menuItemId)
        }

        parentFragmentManager.setFragmentResult(FRAGMENT_ID, result)

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val ARGUMENT_MENU_ID = "com.alexpaxom.ARGUMENT_MENU_ID"

        const val RESULT_PARAM_MENU_ITEM_ID = "com.alexpaxom.RESULT_PARAM_MENU_ITEM_ID"
        const val RESULT_PARAM_CALLBACK_PARAMS = "com.alexpaxom.RESULT_PARAM_CALLBACK_PARAMS"

        const val FRAGMENT_ID = "com.alexpaxom.BOTTOM_MESSAGE_MENU_FRAGMENT"

        @JvmStatic
        fun newInstance(menuId: Int, callbackParams: Parcelable) =
            BottomMenuFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARGUMENT_MENU_ID, menuId)
                    putParcelable(RESULT_PARAM_CALLBACK_PARAMS, callbackParams)
                }
            }
    }
}