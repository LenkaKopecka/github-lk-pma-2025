package com.example.myapp013aeducationgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapp013aeducationgame.databinding.FragmentMainMenuBinding

class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainMenuViewModel::class.java)

        viewModel.latestPlayer.observe(viewLifecycleOwner) {
            it?.let {
                binding.playerNameEditText.setText(it.name)
            }
        }

        binding.startGameButton.setOnClickListener {
            val playerName = binding.playerNameEditText.text.toString()
            if (playerName.isNotBlank()) {
                viewModel.getOrCreatePlayer(playerName) { playerId ->
                    val action = MainMenuFragmentDirections.actionMainMenuFragmentToGameFragment(playerId)
                    findNavController().navigate(action)
                }
            } else {
                binding.playerNameLayout.error = "Please enter your name"
            }
        }

        binding.viewResultsButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_resultsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
