package uk.co.jakelee.pixelbookshop.ui.customise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import uk.co.jakelee.pixelbookshop.R

class CustomiseFragment : Fragment() {

    private lateinit var customiseViewModel: CustomiseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        customiseViewModel =
            ViewModelProviders.of(this).get(CustomiseViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_customise, container, false)
        val textView: TextView = root.findViewById(R.id.text_customise)
        customiseViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}