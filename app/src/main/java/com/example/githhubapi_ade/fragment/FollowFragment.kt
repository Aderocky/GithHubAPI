package com.example.githhubapi_ade.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githhubapi_ade.adapter.FllwAdapter
import com.example.githhubapi_ade.data.response.FollowResponseItem
import com.example.githhubapi_ade.databinding.FragmentFollowBinding
import com.example.githhubapi_ade.viewModel.FllwViewModel


class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowBinding.bind(view)

        val followViewModel = ViewModelProvider(this)[FllwViewModel::class.java]

        followViewModel.listFollow.observe(viewLifecycleOwner) { listUser ->
            if (listUser != null) setFllw(listUser)
        }

        followViewModel.errorText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { theMsg ->
                Toast.makeText(context, theMsg, Toast.LENGTH_LONG).show()
            }
        }

        followViewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading != null) showLoading(loading)
        }

        followViewModel.empty.observe(viewLifecycleOwner) { empty ->
            noFollow(empty)
        }

        followViewModel.temp.observe(viewLifecycleOwner) { indikasi ->
            binding.temp.text = indikasi
        }
        val username = arguments?.getString(ARG_USERNAME)
        val position = arguments?.getInt(ARG_POSITION, 0)

        val linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.rvHero.layoutManager = linearLayoutManager

        if (savedInstanceState == null && username != null) {
            followViewModel.showFllw(username, position == 1)
        }

    }


    private fun setFllw(userData: List<FollowResponseItem>) {
        val adapter = FllwAdapter()
        adapter.submitList(userData)
        binding.rvHero.adapter = adapter
    }

    private fun noFollow(noFoll: Boolean) {
        if (noFoll) {
            binding.rvHero.visibility = View.GONE
            binding.temp.visibility = View.VISIBLE
        } else {
            binding.rvHero.visibility = View.VISIBLE
            binding.temp.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbHero.visibility = View.VISIBLE
            binding.tvMohon.visibility = View.VISIBLE
        } else {
            binding.pbHero.visibility = View.GONE
            binding.tvMohon.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_USERNAME: String = "ARG_USERNAME"
        const val ARG_POSITION: String = "ARG_POSITION"
    }
}