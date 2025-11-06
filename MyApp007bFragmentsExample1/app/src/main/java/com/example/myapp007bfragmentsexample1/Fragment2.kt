package com.example.myapp007afragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragment2: detail obrázku, zachovává ARG_PARAMy a factory newInstance.
 *
 * param1 = image resource id (as String) e.g. "2131230890"
 * param2 = popisek / název obrázku
 */
class Fragment2 : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var imageRes: Int = 0
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // pokus o parsování resource id pokud bylo předáno jako string
        param1?.toIntOrNull()?.let {
            imageRes = it
        }

        title = param2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment (fragment_2.xml)
        val view = inflater.inflate(R.layout.fragment_2, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageViewDetail)
        val textDesc = view.findViewById<TextView>(R.id.textDescription)

        if (imageRes != 0) {
            imageView.setImageResource(imageRes)
        } else {
            // pokud resource id nebylo předáno správně, nastav default (volitelně)
            imageView.setImageResource(R.drawable.img1)
        }

        textDesc.text = title ?: getString(R.string.default_description)

        return view
    }

    companion object {
        /**
         * newInstance(param1, param2)
         * param1: image resource id as String
         * param2: title / description
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
