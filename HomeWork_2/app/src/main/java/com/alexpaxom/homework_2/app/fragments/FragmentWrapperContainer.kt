package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.alexpaxom.homework_2.R
import com.alexpaxom.homework_2.databinding.FragmentWrapContainerBinding

class FragmentWrapperContainer : ViewBindingFragment<FragmentWrapContainerBinding>() {

    override var _binding: Lazy<FragmentWrapContainerBinding>? = lazy {
        FragmentWrapContainerBinding.inflate(layoutInflater)
    }

    private var innerFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            replaceNavigate(
                when(arguments?.getString(PARAM_FRAGMENT_CLASS_NAME)) {
                    ChannelsFragment.FRAGMENT_ID -> ChannelsFragment.newInstance()
                    UsersFragment.FRAGMENT_ID -> UsersFragment.newInstance()
                    else -> error("Bad type of inner fragment")
                }, "innerFragment", true)
        }
        super.onCreate(savedInstanceState)
    }

    fun replaceNavigate(fragment: Fragment, nameBackstack: String, addToBackstack: Boolean = false) {
        val transaction = childFragmentManager.beginTransaction().replace(R.id.wrap_container, fragment, nameBackstack)

        if(addToBackstack) {
            transaction.addToBackStack(nameBackstack)
        }

        transaction.commit()
    }

    fun addNavigate(fragment: Fragment, nameBackstack: String, addToBackstack: Boolean = false) {
            val transaction = childFragmentManager.beginTransaction()
                .add(R.id.wrap_container, fragment, nameBackstack)

            if (addToBackstack) {
                transaction.addToBackStack(nameBackstack)
            }

            transaction.commit()
    }

    companion object {
        const val PARAM_FRAGMENT_CLASS_NAME = "com.alexpaxom.PARAM_FRAGMENT_CLASS_NAME"

        fun newInstance(innerFragmentName: String)
        = FragmentWrapperContainer().apply {
            arguments = Bundle().apply {
                putString(PARAM_FRAGMENT_CLASS_NAME, innerFragmentName)
            }
        }
    }
}