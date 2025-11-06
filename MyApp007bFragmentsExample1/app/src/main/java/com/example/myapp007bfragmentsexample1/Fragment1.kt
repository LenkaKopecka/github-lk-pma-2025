package com.example.myapp007afragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

// stejné konstanty jako v zadání
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragment1: galerie (zachovává šablonu ARG_PARAM1 / ARG_PARAM2 a factory newInstance).
 *
 * param1 = počáteční index (String), např. "0"
 * param2 = název / popisek galerie (String)
 */
class Fragment1 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    // seznam obrázků v res/drawable
    private val images = listOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3
    )

    private val titles = listOf(
        "Květina v trávě",
        "Horský výhled",
        "Městské ulice"
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // pokud byl předán počáteční index jako string, použij ho
        param1?.toIntOrNull()?.let { idx ->
            if (idx in images.indices) currentIndex = idx
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflatuj layout fragment_1.xml (galerie)
        val view = inflater.inflate(R.layout.fragment_1, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageViewGallery)
        val textIndex = view.findViewById<TextView>(R.id.textIndex)
        val btnPrev = view.findViewById<Button>(R.id.btnPrev)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val btnOpenDetail = view.findViewById<Button>(R.id.btnOpenDetail)

        fun updateUI() {
            imageView.setImageResource(images[currentIndex])
            textIndex.text = "${currentIndex + 1} / ${images.size}"
        }

        updateUI()

        btnPrev.setOnClickListener {
            currentIndex = if (currentIndex - 1 < 0) images.size - 1 else currentIndex - 1
            updateUI()
        }

        btnNext.setOnClickListener {
            currentIndex = (currentIndex + 1) % images.size
            updateUI()
        }

        btnOpenDetail.setOnClickListener {
            // použijeme Fragment2.newInstance(imageResAsString, title)
            val imgResString = images[currentIndex].toString()
            val title = titles.getOrNull(currentIndex) ?: param2 ?: getString(R.string.default_description)
            val detail = Fragment2.newInstance(imgResString, title)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detail)
                .commit()
        }

        return view
    }

    companion object {
        /**
         * Zachovaná factory metoda se stejnými parametry jako v zadání.
         * param1 = start index (String), param2 = popisek/název
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
