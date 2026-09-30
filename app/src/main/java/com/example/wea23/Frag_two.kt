package com.example.wea23

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wea23.ViewM.MainVM
import com.example.wea23.ui.screens.detail.DayDetailScreen
import com.example.wea23.ui.theme.Wea23Theme

class Frag_two : Fragment() {
    private val viewModel: MainVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Wea23Theme {
                    DayDetailScreen(
                        viewModel = viewModel,
                        onBack = {
                            findNavController().navigate(R.id.action_frag_two_to_frag_one)
                        }
                    )
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Frag_two()
    }
}
