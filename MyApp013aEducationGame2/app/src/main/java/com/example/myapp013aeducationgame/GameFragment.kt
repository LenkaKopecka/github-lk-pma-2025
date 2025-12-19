package com.example.myapp013aeducationgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapp013aeducationgame.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: GameViewModel
    private val args: GameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = GameViewModelFactory(requireActivity().application, args.playerId)
        viewModel = ViewModelProvider(this, factory).get(GameViewModel::class.java)

        viewModel.score.observe(viewLifecycleOwner) {
            binding.scoreTextView.text = "Score: $it"
        }

        viewModel.currentQuestion.observe(viewLifecycleOwner) {
            it?.let {
                binding.questionTextView.text = it.questionText
                val answers = listOf(it.correctAnswer, it.wrongAnswer1, it.wrongAnswer2).shuffled()
                binding.answerButton1.text = answers[0]
                binding.answerButton2.text = answers[1]
                binding.answerButton3.text = answers[2]
            }
        }

        viewModel.gameFinished.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_gameFragment_to_resultsFragment)
            }
        }

        binding.answerButton1.setOnClickListener { viewModel.onAnswerSelected(binding.answerButton1.text.toString()) }
        binding.answerButton2.setOnClickListener { viewModel.onAnswerSelected(binding.answerButton2.text.toString()) }
        binding.answerButton3.setOnClickListener { viewModel.onAnswerSelected(binding.answerButton3.text.toString()) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
