package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class ViewBindingFragment<B: ViewBinding> : Fragment() {
    private var _binding: B? = null
    protected val binding get() = getOrCreateBinding()

    protected abstract fun createBinding(): B

    private fun getOrCreateBinding(): B {
        if(_binding == null)
            _binding = createBinding()

        return _binding!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}