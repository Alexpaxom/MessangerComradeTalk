package com.alexpaxom.homework_2.app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexpaxom.homework_2.app.adapters.ChatHistoryAdapter
import com.alexpaxom.homework_2.data.models.Message
import com.alexpaxom.homework_2.data.repositories.TestMessagesRepository
import com.alexpaxom.homework_2.databinding.FragmentChatBinding


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance() = ChatFragment()
    }
}