package com.example.myapp013aeducationgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp013aeducationgame.databinding.FragmentResultsBinding

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ResultsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ResultsViewModel::class.java)

        val adapter = ResultsAdapter()
        binding.resultsRecyclerView.adapter = adapter
        binding.resultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allResults.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.noResultsTextView.visibility = View.VISIBLE
                binding.resultsRecyclerView.visibility = View.GONE
            } else {
                binding.noResultsTextView.visibility = View.GONE
                binding.resultsRecyclerView.visibility = View.VISIBLE
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
